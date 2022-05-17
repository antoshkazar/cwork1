import FindI.*;
import graphicForm.GraphicJDialog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.Vector;

/**
 * Класс основного окна.
 */
public class MainForm extends JDialog {
    private JPanel contentPane;
    private JButton chooseFileButton;
    private JButton graphicButton;
    private JButton peaksButton;
    private JScrollPane scrollTable;
    private DefaultTableModel modelIdeal;
    private JTable idealsTable, peaksComp;
    private JTextArea nameArea;
    private JButton addRowButton;
    private JButton removeButton;
    private JButton clearButton;
    private JButton saveButton;
    private JButton returnButton;
    File fileWithData = null;
    // Первая булевая переменная нужна для сокрытия формы с
    // графиком при нажатии кнопки, вторая - для очистки таблицы с эталонами,
    // третья - для проверки того, открывается ли график впервые
    boolean graphicOpened = false, cleared = false,
            firstTimeOpened = true;
    // Вектор с исходными данными.
    Vector<Double> x = new Vector<>();
    // Вектор с исходными данными.
    Vector<Double> y = new Vector<>();
    // Окно с графиком.
    GraphicJDialog secForm;

    public MainForm() {
        setLocationRelativeTo(null);
        setContentPane(contentPane);
        setModal(true);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getRootPane().setDefaultButton(chooseFileButton);
        addListeners();
    }

    /**
     * Обработчики событий.
     */
    private void addListeners() {
        chooseFileButton.setText("Выберите файл ");
        chooseFileButton.addActionListener(e -> chooseFileButtonPressed());
        peaksButton.addActionListener(e -> {
            addPeak();
        });
        graphicButton.setText("Показать/скрыть график");
        graphicButton.addActionListener(e -> {
            // getFileData();
            graphic();
        });
        addRowButton.addActionListener(e -> addRow());
        removeButton.addActionListener(e -> removeRow());
        clearButton.addActionListener(e -> clearTable());
        saveButton.addActionListener(e -> exportToCSV());
        returnButton.addActionListener(e -> {
            clearTable();
            addRows();
        });
        makeTable();
    }

    /**
     * Добавление информации о пике в таблицу.
     */
    private void addPeak() {
        try {
            String input = JOptionPane.showInputDialog("Введите координаты начала и конца пика по Y, разделяя знаком \"/\"");
            Object[] arrInput = input.split("/");
            double begin = Double.parseDouble((String) arrInput[0]);
            double end = Double.parseDouble((String) arrInput[1]);
            double minElem = Double.MAX_VALUE, maxElem = Double.MIN_VALUE;
            int posMin = 0, posMax = 0, indOfBegin = 0, indOfEnd = 0;
            for (int i = 0; i < y.size(); i++) {
                if (y.get(i) == begin) {
                    indOfBegin = i;
                } else if (y.get(i) == end) {
                    indOfEnd = i;
                }
            }
            boolean trendDown = begin >= x.get(x.indexOf(begin) + 1);
            for (int i = indOfBegin; i < indOfEnd; i++) {
                if (trendDown) {
                    if (x.get(i) < minElem) {
                        minElem = x.get(i);
                        posMin = i;
                    }
                } else {
                    if (x.get(i) > maxElem) {
                        maxElem = x.get(i);
                        posMax = i;
                    }
                }
            }
            if (trendDown) {
                secForm.getF().peaks.add(new Peak(indOfEnd, indOfBegin, posMin, minElem, true, x, y));
            } else {
                secForm.getF().peaks.add(new Peak(indOfEnd, indOfBegin, posMax, maxElem, false, x, y));
            }
            secForm.graphic();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ошибка ввода! " + e.getMessage());
        }
    }

    /**
     * Очистка таблицы.
     */
    private void clearTable() {
        try {
            if (modelIdeal != null) {
                modelIdeal.setRowCount(0);
                cleared = true;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ошибка " + e.getMessage());
        }
    }

    /**
     * Удаление последнего ряда таблицы.
     */
    private void removeRow() {
        try {
            if (modelIdeal != null) {
                modelIdeal.removeRow(modelIdeal.getRowCount() - 1);
            }
        } catch (IndexOutOfBoundsException e) {
            JOptionPane.showMessageDialog(this, "Этот ряд нельзя удалить!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ошибка " + e.getMessage());
        }
    }

    /**
     * Добавление ряда в таблицу.
     */
    private void addRow() {
        try {
            if (modelIdeal != null) {
                String input = JOptionPane.showInputDialog("Введите строку для таблицы, разделяя столбцы знаком \"/\"");
                Object[] arrInput = input.split("/");
                Integer.parseInt((String) arrInput[0]);
                if (((String) arrInput[2]).contains("-")) {
                    var range = ((String) arrInput[2]).split("-");
                    Integer.parseInt(range[0]);
                    Integer.parseInt(range[1]);
                }
                if (arrInput.length < 4) {
                    throw new Exception();
                }
                modelIdeal.addRow(arrInput);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ошибка ввода!");
        }
    }

    /**
     * Составление таблицы сравнения.
     */
    private void makeCompareTable() {
        try {
            var peaks = secForm.getF().peaks;
            DefaultTableModel peaksComparison = new DefaultTableModel();
            peaksComparison.addColumn("НОМЕР ПИКА");
            peaksComparison.addColumn("I(max)");
            peaksComparison.addColumn("I(инт)");
            peaksComparison.addColumn("v(эксп)");
            peaksComparison.addColumn("v(этал)");
            peaksComparison.addColumn("Отнесение полос");
            for (var peak : peaks) {
                Object[] row = new Object[]{peaks.indexOf(peak), peak.depth, peak.square,
                        y.get(peak.indexExtremum), " ", " "};
                for (int i = 0; i < modelIdeal.getRowCount(); i++) {
                    try {
                        String ideal = modelIdeal.getValueAt(i, 2).toString();
                        if (ideal.contains("-")) {
                            var splitted = ideal.split("-");
                            if (y.get(peak.indexExtremum) > Integer.parseInt(splitted[0]) &&
                                    y.get(peak.indexExtremum) < Integer.parseInt(splitted[1])) {
                                row[4] = ideal;
                                row[5] = modelIdeal.getValueAt(i, 3).toString();
                                peaksComparison.addRow(row);
                                break;
                            }
                        } else {
                            if (y.get(peak.indexExtremum) == Integer.parseInt(ideal)) {
                                row[4] = ideal;
                                row[5] = modelIdeal.getValueAt(i, 3).toString();
                                peaksComparison.addRow(row);
                                break;
                            }
                        }

                    } catch (Exception ignored) {
                    }
                }
            }
            peaksComp = new JTable(peaksComparison);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    /**
     * Сохранение таблицы сравнений.
     *
     * @return
     */
    private boolean exportToCSV() {
        try {
            makeCompareTable();
            var model = (DefaultTableModel) peaksComp.getModel();
            FileWriter csv = new FileWriter(new File("saved.csv"));
            for (int i = 0; i < model.getColumnCount(); i++) {
                csv.write(model.getColumnName(i) + ",");
            }
            csv.write("\n");
            for (int i = 0; i < model.getRowCount(); i++) {
                for (int j = 0; j < model.getColumnCount(); j++) {
                    csv.write(model.getValueAt(i, j).toString() + ",");
                }
                csv.write("\n");
            }
            csv.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Исходное заполнение таблицы.
     */
    private void addRows() {
        try {
            if (cleared) {
                Object[] row = new Object[]{"1", "ν(OH)", "3600-3700",
                        "Кислотные ОН-группы мостиковых гидроксилов Al-OH-O или Si-OH-Al (центры Бренстеда)"};
                modelIdeal.addRow(row);
                row = new Object[]{"2", "v(OH)", "3200-3600", "ОН-группы каркасных Si-OH группировок," +
                        " либо молекулы H2O"};
                modelIdeal.addRow(row);
                row = new Object[]{"3", "νas(CH2) или νs(CH3) или ν??(CH2) или δ(CH3)", "2855",
                        "Остатки органического темплата (TPAOH) в полостях каркаса TS " +
                                "(асимметричные –CH2, симметричные –CH3 и растягивающие –CH2 колебания;" +
                                " деформационные колебания –CH3)"};
                modelIdeal.addRow(row);
                row = new Object[]{"3", "νas(CH2) или νs(CH3) или ν??(CH2) или δ(CH3)", "2930-2960",
                        "Остатки органического темплата (TPAOH) в полостях каркаса TS " +
                                "(асимметричные –CH2, симметричные –CH3 и растягивающие –CH2 колебания;" +
                                " деформационные колебания –CH3)"};
                modelIdeal.addRow(row);
                row = new Object[]{"4", "ν(CO2)", "2300", "Сорбированные молекулы CO2 в порах " +
                        "с наибольшими размерами на поверхности"};
                modelIdeal.addRow(row);
                row = new Object[]{"5", "δ H–O–H", "1630-1635", "-Деформационные колебания H–O–H"};
                modelIdeal.addRow(row);
                row = new Object[]{"6", "νas(ТO4)", "1225-1230", "Асимметричные валентные колебания внутри TS"};
                modelIdeal.addRow(row);
                row = new Object[]{"7", "νas(ТO4)", "1108-1110", "Асимметричные валентные колебания внутри тетраэдров ТO4"};
                modelIdeal.addRow(row);
                row = new Object[]{"8", " ", "960 - 970", "Титан в каркасе TS"};
                modelIdeal.addRow(row);
                row = new Object[]{"9", "νs(ТO4)", "800", "Симметричные валентные колебания внутри тетраэдров ТO4"};
                modelIdeal.addRow(row);
                row = new Object[]{"10", "νs(SiOSi)+δ(OSiO), ν(Al-OH)", "550", "Указывает на принадлежность цеолита к семейству ZSM"};
                modelIdeal.addRow(row);
                row = new Object[]{"11", "δ(TO4)", "450", "Деформационные колебания связей Т–O"};
                modelIdeal.addRow(row);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    /**
     * Создание исходника таблицы с эталонами.
     */
    private void makeTable() {
        modelIdeal = (DefaultTableModel) idealsTable.getModel();
        modelIdeal.addColumn("ЭТАЛОНЫ");
        modelIdeal.addColumn("v");
        modelIdeal.addColumn("см^(-1)");
        modelIdeal.addColumn("Отнесение полос");
        cleared = true;
        addRows();
        cleared = false;
    }

    /**
     * Вызов окна для построения графика.
     */
    private void graphic() {
        try {
            if (!graphicOpened) {
                if (firstTimeOpened) {
                    secForm = new GraphicJDialog(x, y, "TS-1P75.dat"); //TODO ПОМЕНЯТЬ НА ВЫБРАННЫЙ ГРАФИК
                    secForm.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                    secForm.setResizable(true);
                    secForm.setVisible(true);
                    graphicOpened = true;
                    firstTimeOpened = false;
                } else {
                    graphicOpened = true;
                    secForm.setResizable(true);
                    secForm.setVisible(true);
                }
            } else {
                secForm.setVisible(false);
                graphicOpened = false;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    /**
     * Чтение информации из выбранного файла.
     */
    private void getFileData() {
        if (fileWithData != null) {
        try {
            FileReader fr = new FileReader(fileWithData); //TODO исправить на получение файла из указанной директории
           // FileReader fr = new FileReader("src/main/java/TS-1P75.dat");
            BufferedReader reader = new BufferedReader(fr);
            String line = reader.readLine();
            while (line != null) {
                var lines = line.split("\t");
                x.add(Double.parseDouble(lines[1]));
                y.add(Double.parseDouble(lines[0]));
                line = reader.readLine();
            }
            EventQueue.invokeLater(() -> {

            });
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Неудалось преобразовать данные!");
            ex.printStackTrace();
        }
        }
    }

    /**
     * Обработчик события нажатия кнопки выбора файла.
     */
    private void chooseFileButtonPressed() {
        try {
            JFileChooser jFileChooser = new JFileChooser();
            jFileChooser.setDialogTitle("Выбор директории");
            jFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int result = jFileChooser.showOpenDialog(this);
            fileWithData = jFileChooser.getSelectedFile();
            nameArea.setText("Выбранный файл:\n" + fileWithData.getName());
            getFileData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        MainForm dialog = new MainForm();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }

}
