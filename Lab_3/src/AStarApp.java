package laba3;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class AStarApp {

    /** Количество ячеек сетки в направлении X **/
    private int width;

    /** Количество ячеек сетки в направлении Y. **/
    private int height;

    /** Место, откуда начинается путь. **/
    private Location startLoc;

    /** Место, где путь должен заканчиваться. **/
    private Location finishLoc;

    /**
      Это двухмерный массив компонентов пользовательского интерфейса, которые обеспечивают отображение и управление ячеек на карте.**/
    private JMapCell[][] mapCells;


    /**
     * Этот внутренний класс обрабатывает события мыши в основной сетке ячеек карты,
     изменение ячеек в зависимости от состояния кнопки мыши и первоначального редактирования
     что было выполнено.
     **/
    private class MapCellHandler implements MouseListener
    {
        /**
         * Это значение будет истинным, если кнопка мыши была нажата, и мы в данный момент находимся в середине операции модификации.
         **/
        private boolean modifying;

        /**
         *Это значение указывает, делаем ли мы ячейки проходимыми или непроходимыми.
         * Какой именно, зависит от исходного состояния ячейки, в которой была запущена операция.
         **/
        private boolean makePassable;

        /** Initiates the modification operation. **/
        public void mousePressed(MouseEvent e)
        {
            modifying = true;

            JMapCell cell = (JMapCell) e.getSource();

            // If the current cell is passable then we are making them
            // impassable; if it's impassable then we are making them passable.

            makePassable = !cell.isPassable();

            cell.setPassable(makePassable);
        }

        /** Ends the modification operation. **/
        public void mouseReleased(MouseEvent e)
        {
            modifying = false;
        }

        /**
         * If the mouse has been pressed, this continues the modification
         * operation into the new cell.
         **/
        public void mouseEntered(MouseEvent e)
        {
            if (modifying)
            {
                JMapCell cell = (JMapCell) e.getSource();
                cell.setPassable(makePassable);
            }
        }

        public void mouseExited(MouseEvent e)
        {
            // This one we ignore.
        }

        public void mouseClicked(MouseEvent e)
        {
            // And this one too.
        }
    }

    public AStarApp(int w, int h) {
        if (w <= 0)
            throw new IllegalArgumentException("w must be > 0; got " + w);

        if (h <= 0)
            throw new IllegalArgumentException("h must be > 0; got " + h);

        width = w;
        height = h;

        startLoc = new Location(2, h / 2);
        finishLoc = new Location(w - 3, h / 2);
    }


    private void initGUI()
    {
        JFrame frame = new JFrame("Pathfinder");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container contentPane = frame.getContentPane();

        contentPane.setLayout(new BorderLayout());


        GridBagLayout gbLayout = new GridBagLayout();
        GridBagConstraints gbConstraints = new GridBagConstraints();
        gbConstraints.fill = GridBagConstraints.BOTH;
        gbConstraints.weightx = 1;
        gbConstraints.weighty = 1;
        gbConstraints.insets.set(0, 0, 1, 1);

        JPanel mapPanel = new JPanel(gbLayout);
        mapPanel.setBackground(Color.GRAY);

        mapCells = new JMapCell[width][height];

        MapCellHandler cellHandler = new MapCellHandler();

        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
                mapCells[x][y] = new JMapCell();

                gbConstraints.gridx = x;
                gbConstraints.gridy = y;

                gbLayout.setConstraints(mapCells[x][y], gbConstraints);

                mapPanel.add(mapCells[x][y]);
                mapCells[x][y].addMouseListener(cellHandler);
            }
        }

        contentPane.add(mapPanel, BorderLayout.CENTER);

        JButton findPathButton = new JButton("Find Path");
        findPathButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { findAndShowPath(); }
        });

        contentPane.add(findPathButton, BorderLayout.SOUTH);

        frame.pack();
        frame.setVisible(true);

        mapCells[startLoc.xCoord][startLoc.yCoord].setEndpoint(true);
        mapCells[finishLoc.xCoord][finishLoc.yCoord].setEndpoint(true);
    }

    private void start()
    {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() { initGUI(); }
        });
    }


    private void findAndShowPath()
    {
        // Create a Map2D object containing the current state of the user input.

        Map2D map = new Map2D(width, height);
        map.setStart(startLoc);
        map.setFinish(finishLoc);

        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
                mapCells[x][y].setPath(false);

                if (mapCells[x][y].isPassable())
                    map.setCellValue(x, y, 0);
                else
                    map.setCellValue(x, y, Integer.MAX_VALUE);
            }
        }

        // Try to compute a path.  If one can be computed, mark all cells in the
        // path.

        Waypoint wp = AStarPathfinder.computePath(map);

        while (wp != null)
        {
            Location loc = wp.getLocation();
            mapCells[loc.xCoord][loc.yCoord].setPath(true);

            wp = wp.getPrevious();
        }
    }

    public static void main(String[] args) {
        AStarApp app = new AStarApp(40, 30);
        app.start();
    }
}
