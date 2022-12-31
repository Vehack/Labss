import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import javax.imageio.ImageIO;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.awt.event.*;

public class FractalExplorer {
    private int displaySize;
    private JImageDisplay display;
    private FractalGenerator fractal;
    private Rectangle2D.Double range;

    /**
     * Конструктор принимает размер окна и инициализирует все поля класса
     */
    public FractalExplorer(int size) {
        displaySize = size;
        // Фрактал по умолчанию
        fractal = new Mandelbrot();
        range = new Rectangle2D.Double();
        fractal.getInitialRange(range);
        display = new JImageDisplay(displaySize, displaySize);
    }

    /**
     * Этот метод инициализирует Swing GUI из полей находящихся в классе
     * Создает интерфейс
     */
    public void createAndShowGUI() {
        display.setLayout(new BorderLayout());
        // Создаем окно с заголовком
        JFrame frame = new JFrame("Fractal Explorer");
        frame.add(display, BorderLayout.CENTER);
        // Создаем кнопку
        JButton resetButton = new JButton("Reset scale");
        resetButton.setActionCommand("Reset");
        // Обработчик для конпки сброса
        ButtonHandler handler = new ButtonHandler();
        resetButton.addActionListener(handler);

        // Кнопка сохранения
        JButton saveButton = new JButton("Save");
        ButtonHandler saveHandler = new ButtonHandler();
        saveButton.addActionListener(saveHandler);

        JPanel southPanel = new JPanel();
        southPanel.add(saveButton);
        southPanel.add(resetButton);

        frame.add(southPanel, BorderLayout.SOUTH);
        // Обработчик для клика мыши по фракталу
        MouseHandler click = new MouseHandler();
        display.addMouseListener(click);
        // Окно закрывается только при нажатие крестика
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Выпадающее окно
        JComboBox<FractalGenerator> selectFractal = new JComboBox<>();

        FractalGenerator mandelbrot = new Mandelbrot();
        selectFractal.addItem(mandelbrot);
        FractalGenerator tricorn = new Tricorn();
        selectFractal.addItem(tricorn);
        FractalGenerator burningship = new BurningShip();
        selectFractal.addItem(burningship);

        ButtonHandler fractalChooser = new ButtonHandler();
        selectFractal.addActionListener(fractalChooser);

        JPanel panel = new JPanel();
        JLabel label = new JLabel("Выберите фрактал");
        panel.add(label);
        panel.add(selectFractal);
        frame.add(panel, BorderLayout.NORTH);

        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
    }

    /*
     * Метод для отрисовки фрактала
     * Окрашивает пиксели в зависимотси от итерации
     */
    private void drawFractal() {
        for (int x = 0; x < displaySize; x++) {
            for (int y = 0; y < displaySize; y++) {

                double xCoord = FractalGenerator.getCoord(range.x, range.x +
                        range.width, displaySize, x);
                double yCoord = FractalGenerator.getCoord(range.y, range.y +
                        range.height, displaySize, y);

                int iteration = fractal.numIterations(xCoord, yCoord);

                if (iteration == -1) {
                    display.drawPixel(x, y, 0);
                } else {
                    float hue = 0.7f + (float) iteration / 200f;
                    int rgbColor = Color.HSBtoRGB(hue, 1f, 1f);
                    display.drawPixel(x, y, rgbColor);
                }
            }
        }
        display.repaint();
    }

    private class ButtonHandler implements ActionListener {

        @Override // метод созданный по умолчанию
        public void actionPerformed(ActionEvent e) {
            // Возващает "Reset" для reset'a или "Save"
            String comand = e.getActionCommand();
            // Возвращает фрактал к изначальному положению
            if (e.getSource() instanceof JComboBox) {
                JComboBox<FractalGenerator> source = (JComboBox<FractalGenerator>) e.getSource();
                fractal = (FractalGenerator) source.getSelectedItem();
                fractal.getInitialRange(range);
                drawFractal();
                System.out.println("Choosen another fractal");
            } else if (comand.equals("Reset")) {
                fractal.getInitialRange(range);
                drawFractal();
                System.out.println("Reseted");
            } else if (comand.equals("Save")) {
                // создает окно для выбора места сохранения файла
                JFileChooser chooser = new JFileChooser();
                // сохраянет только png
                FileFilter filter = new FileNameExtensionFilter(
                        "PNG Images, *.png", "png");
                chooser.setFileFilter(filter);
                // и не показывает ничего кроме png и папок
                chooser.setAcceptAllFileFilterUsed(false);
                // отображает всплывающее окно / 0 если файл указан
                // 1 если пользователь передумал
                int userSelection = chooser.showSaveDialog(display);
                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    // Путь до файла
                    File file = chooser.getSelectedFile();
                    String file_name = file.toString() + ".png";
                    File fileWithExt = new File(file_name);
                    try {
                        BufferedImage renderedImage = display.getImage();
                        ImageIO.write(renderedImage, "png", fileWithExt);
                    } catch (Exception err) {
                        JOptionPane.showMessageDialog(
                                display,
                                err.getMessage(),
                                "Cannot Save Image",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
    }

    /*
     * Приближает фрактал в точку клика
     */
    private class MouseHandler extends MouseAdapter {
        public void mouseClicked(MouseEvent e) {
            // Координаты клика
            int x = e.getX();
            int y = e.getY();
            // Новые координаты центра
            double xCoord = FractalGenerator.getCoord(range.x,
                    range.x + range.width, displaySize, x);
            double yCoord = FractalGenerator.getCoord(range.y,
                    range.y + range.height, displaySize, y);

            // Устанавливаем центр в точку по которой был клик и приближаем
            fractal.recenterAndZoomRange(range, xCoord, yCoord, 0.5);
            // Перерисовываем
            drawFractal();
        }
    }

    public static void main(String[] args) {
        FractalExplorer fractal = new FractalExplorer(600);
        fractal.createAndShowGUI();
        fractal.drawFractal();
    }
}

