package gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import log.Logger;

/**
 * Что требуется сделать:
 * 1. Метод создания меню перегружен функционалом и трудно читается. 
 * Следует разделить его на серию более простых методов (или вообще выделить отдельный класс).
 *
 */
public class MainApplicationFrame extends JFrame
{
    private final JDesktopPane desktopPane = new JDesktopPane();
    
    public MainApplicationFrame() 
    {
        //Make the big window be indented 50 pixels from each edge
        //of the screen.
        int inset = 50;        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset,
            screenSize.width  - inset*2,
            screenSize.height - inset*2);

        setContentPane(desktopPane);
        
        
        LogWindow logWindow = createLogWindow();
        addWindow(logWindow);

        GameWindow gameWindow = new GameWindow();
        gameWindow.setSize(400,  400);
        addWindow(gameWindow);

        setJMenuBar(generateMenuBar());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        JMenuBar menuBar = generateMenuBar();
    	menuBar.add(createMenuBar());
    	setJMenuBar(menuBar);
    }
    
    protected LogWindow createLogWindow()
    {
        LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource());
        logWindow.setLocation(10,10);
        logWindow.setSize(300, 800);
        setMinimumSize(logWindow.getSize());
        logWindow.pack();
        Logger.debug("Протокол работает");
        return logWindow;
    }
    
    protected void addWindow(JInternalFrame frame)
    {
        desktopPane.add(frame);
        frame.setVisible(true);
    }
    
    protected JMenuBar createMenuBar() 
    {
        JMenuBar menuBar = new JMenuBar();
 
        //Set up the lone menu.
        JMenu menu = new JMenu("Документ");
        menu.setMnemonic(KeyEvent.VK_D);
        menuBar.add(menu);
 
        //Set up the first menu item.
        JMenuItem menuItem = new JMenuItem("Новый");
        menuItem.setMnemonic(KeyEvent.VK_N);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_N, ActionEvent.ALT_MASK));
        menuItem.setActionCommand("new");
        menuItem.addActionListener((event) -> {});
        menu.add(menuItem);
 
        //Set up the second menu item.
        menuItem = new JMenuItem("Выход");
        menuItem.setMnemonic(KeyEvent.VK_Q);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_Q, ActionEvent.ALT_MASK));
        menuItem.setActionCommand("quit");
        menuItem.addActionListener((event) -> {exitWindow();});
        menu.add(menuItem);
 
        return menuBar;
    }
    
    protected void exitWindow()
    { 
    	Object[] options = { "Да", "Нет" }; 
    	int sel = JOptionPane.showOptionDialog(null, "Вы уверены, что хотите выйти?", 
    	"Выход", JOptionPane.DEFAULT_OPTION, 
    	JOptionPane.WARNING_MESSAGE, null, options, options[0]); 
    	if (sel == JOptionPane.YES_OPTION) 
    	{ 
    	System.exit(0); 
    	} 
    }
    
	protected void writeInFile(String s)
    {
    	File f = new File(System.getProperty("user.home"), "location.txt");
    	DataOutputStream locations;
    	try
    	{
    		locations = new DataOutputStream(new FileOutputStream(f));
    		locations.writeChars(s);
    	}
    	catch(Exception e)
    	{
    		System.exit(0);
    	}
    }
    
    private JMenuBar generateMenuBar()
    {
        JMenuBar menuBar = new JMenuBar();
        
        JMenu lookAndFeelMenu = new JMenu("Режим отображения");
        lookAndFeelMenu.setMnemonic(KeyEvent.VK_V);
        lookAndFeelMenu.getAccessibleContext().setAccessibleDescription(
                "Управление режимом отображения приложения");
        
        {
        	JMenuItem systemLookAndFeel = createMenuItem("Системная схема", UIManager.getSystemLookAndFeelClassName());
            lookAndFeelMenu.add(systemLookAndFeel);
        }

        {
            JMenuItem crossplatformLookAndFeel = createMenuItem("Универсальная схема", UIManager.getCrossPlatformLookAndFeelClassName());
            lookAndFeelMenu.add(crossplatformLookAndFeel);
        }
        
        JMenu testMenu = new JMenu("Тесты");
        testMenu.setMnemonic(KeyEvent.VK_T);
        testMenu.getAccessibleContext().setAccessibleDescription(
                "Тестовые команды");
        
        {
            JMenuItem addLogMessageItem = new JMenuItem("Сообщение в лог", KeyEvent.VK_S);
            addLogMessageItem.addActionListener((event) -> {
                Logger.debug("Новая строка");
            });
            testMenu.add(addLogMessageItem);
        }

        menuBar.add(createFileMenu());
        menuBar.add(lookAndFeelMenu);
        menuBar.add(testMenu);
        return menuBar;
    }
    
    private JMenu createFileMenu() 
    {
    	JMenu fileMenu = new JMenu("Файл");
    	fileMenu.setMnemonic(KeyEvent.VK_F);
    	fileMenu.getAccessibleContext().setAccessibleDescription("Общие команды");
    	JMenuItem exitMenuItem = new JMenuItem("Выйти", KeyEvent.VK_X);
    	exitMenuItem.addActionListener((event) -> {
    	});
    	fileMenu.add(exitMenuItem);
    	return fileMenu;
    }
    private JMenuItem createMenuItem(String text, String className)
    {
    	JMenuItem systemLookAndFeel = new JMenuItem(text, KeyEvent.VK_S);
    	systemLookAndFeel.addActionListener((event) -> {
    		setLookAndFeel(className);
    		this.invalidate();
    		});
    	return systemLookAndFeel;
    }
    
    private void setLookAndFeel(String className)
    {
        try
        {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(this);
        }
        catch (ClassNotFoundException | InstantiationException
            | IllegalAccessException | UnsupportedLookAndFeelException e)
        {
            // just ignore
        }
    }
}
