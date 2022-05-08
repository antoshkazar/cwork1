import FindI.*;
import graphicForm.graphicJDialog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import java.awt.*;
import java.io.*;
import java.util.Vector;


public class MainForm extends JDialog {
    private JPanel contentPane;
    private JTextField fileName;
    private JButton chooseFileButton;
    private JButton graphicButton;
    private JButton peaksButton;
    private JScrollPane scrollTable;
    private DefaultTableModel modelIdeal;
    private JTable idealsTable;
    private JTextArea nameArea;
    private JButton addRowButton;
    private JButton buttonCancel;
    File fileWithDataName = null;
    boolean graphicOpened = false;
    Vector<Double> x = new Vector<>();
    Vector<Double> y = new Vector<>();
    graphicJDialog secform;

    public MainForm() {
        //setVisible(false);
        //setSize(600,600);
        setLocationRelativeTo(null);
        //setResizable(false);
        //contentPane.setSize(500,500);
        setContentPane(contentPane);
        setModal(true);
        //graphicPanel.setSize(10000,10000);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getRootPane().setDefaultButton(chooseFileButton);
        //setModal(true);
        addListeners();
    }

    private void addListeners() {
        chooseFileButton.setText("Выберите файл");
        chooseFileButton.addActionListener(e -> chooseFileButtonPressed());
        peaksButton.addActionListener(e -> {
            findI f = new findI(x, y);
        });
        graphicButton.setText("Показать/скрыть график");
        graphicButton.addActionListener(e -> {
            getFileData(); //TODO УБРАТЬ
            graphic();
        });
        addRowButton.addActionListener(e -> {
            addRow();
        });
        makeTable();
    }

    private void addRow() {
        try {
            if (modelIdeal == null) {
                return;
            } else {
                String input = JOptionPane.showInputDialog("Введите строку для таблицы, разделяя столбцы знаком \"/\"");
                Object[] arrInput = input.split("/");
                if (arrInput.length < 4) {
                    throw new Exception();
                }
                modelIdeal.addRow(arrInput);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ошибка ввода!");
        }
    }

    private void makeTable() {
        modelIdeal = (DefaultTableModel) idealsTable.getModel();
        modelIdeal.addColumn("Эталоны");
        modelIdeal.addColumn("v");
        modelIdeal.addColumn("см^(-1)");
        modelIdeal.addColumn("Отнесение полос");
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

    private void graphic() {
        try {
            if (!graphicOpened) {
                secform = new graphicJDialog(x, y, "TS-1P.dat"); //TODO ПОМЕНЯТЬ НА ВЫБРАННЫЙ ГРАФИК
                secform.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                secform.setResizable(true);
                secform.setVisible(true);
                graphicOpened = true;
            } else {
                secform.dispose();
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
            FileReader fr = new FileReader("src/main/java/TS-1P.dat");
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
            fileWithDataName = jFileChooser.getSelectedFile();
            nameArea.setText("Выбранный файл:\n" + fileWithDataName.getName());
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
