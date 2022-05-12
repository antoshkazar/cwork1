import FindI.*;
import graphicForm.GraphicJDialog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.Vector;


public class MainForm extends JDialog {
    private JPanel contentPane;
    private JButton chooseFileButton;
    private JButton graphicButton;
    private JButton peaksButton;
    private JScrollPane scrollTable;
    private DefaultTableModel modelIdeal;
    private JTable idealsTable;
    private JTextArea nameArea;
    private JButton addRowButton;
    private JButton removeButton;
    private JButton clearButton;
    private JButton saveButton;
    private JButton returnButton;
    File fileWithData = null;
    boolean graphicOpened = false, cleared = false,
            firstTimeOpened = true;
    Vector<Double> x = new Vector<>();
    Vector<Double> y = new Vector<>();
    GraphicJDialog secForm;

    public MainForm() {
        setLocationRelativeTo(null);
        setContentPane(contentPane);
        setModal(true);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getRootPane().setDefaultButton(chooseFileButton);
        addListeners();
    }

    private void addListeners() {
        chooseFileButton.setText("Выберите файл");
        chooseFileButton.addActionListener(e -> chooseFileButtonPressed());
        peaksButton.addActionListener(e -> {
            FindIntensity f = new FindIntensity(x, y);
        });
        graphicButton.setText("Показать/скрыть график");
        graphicButton.addActionListener(e -> {
            getFileData(); //TODO УБРАТЬ
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
    /*
    private JTable makeCompareTable(){
        try {
            var peaks = secForm.getF().getPeaks();
        }
    }*/

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

    private boolean exportToCSV() {
        try {
            FileWriter csv = new FileWriter(new File("saved.csv"));
            for (int i = 0; i < modelIdeal.getColumnCount(); i++) {
                csv.write(modelIdeal.getColumnName(i) + ",");
            }
            csv.write("\n");
            for (int i = 0; i < modelIdeal.getRowCount(); i++) {
                for (int j = 0; j < modelIdeal.getColumnCount(); j++) {
                    csv.write(modelIdeal.getValueAt(i, j).toString() + ",");
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

    private void graphic() {
        try {
            if (!graphicOpened) {
                if(firstTimeOpened) {
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

    private void getFileData() {
        //if (fileWithDataName != null) {
        try {
            //FileReader fr = new FileReader(fileWithDataName); //TODO исправить на получение файла из указанной директории
            FileReader fr = new FileReader("src/main/java/TS-1P75.dat");
            BufferedReader reader = new BufferedReader(fr);
            String line = reader.readLine();
            while (line != null) {
                var lines = line.split("\t");
                x.add(Double.parseDouble(lines[1]));
                y.add(Double.parseDouble(lines[0]));
                line = reader.readLine();
            }
            EventQueue.invokeLater(new Runnable() {
                public void run() {

                }
            });
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Неудалось преобразовать данные!");
            ex.printStackTrace();
        }
        //}
    }

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
