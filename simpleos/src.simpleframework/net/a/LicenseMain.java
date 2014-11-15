package net.a;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import net.itsite.HomePageAppModule;
import net.itsite.i.IAppModule;
import net.itsite.permission.PlatformMenuItem;
import net.itsite.utils.StringsUtils;
import net.simpleframework.web.page.component.ui.menu.MenuItem;

import org.jdesktop.swingx.VerticalLayout;

public class LicenseMain {
	public static final List<MenuItem> menuList = new ArrayList<MenuItem>();

	public static void registerAppModule(IAppModule appModule) {
		menuList.addAll(appModule.getMenuItems());
		Collections.sort(menuList, new Comparator<MenuItem>() {

			@Override
			public int compare(final MenuItem o1, final MenuItem o2) {
				final PlatformMenuItem menu1 = (PlatformMenuItem) o1;
				final PlatformMenuItem menu2 = (PlatformMenuItem) o2;
				if (menu1.getIndex() > menu2.getIndex()) {
					return 1;
				} else if (menu1.getIndex() < menu2.getIndex()) {
					return -1;
				}
				return 0;
			}
		});
	}

	public static void main(final String[] args) {
		Locale.setDefault(Locale.CHINA);
		registerAppModule(new HomePageAppModule());

		final JFrame main = new JFrame("RAMS License Gen");
		main.setSize(800, 600);
		main.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		final JTextField days = new JTextField("30", 10);
		final JTextField typev = new JTextField("免费版", 15);
		final JTextField version = new JTextField("1.0.0", 15);

		final JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
		top.add(new JLabel("试用期（天）"));
		top.add(days);

		top.add(new JLabel("版本类型"));
		top.add(typev);

		top.add(new JLabel("版本号"));
		top.add(version);

		main.getContentPane().add(top, BorderLayout.NORTH);

		final JPanel modulePanel = new JPanel(new VerticalLayout());

		final List<JCheckBox> cBoxs = new ArrayList<JCheckBox>();
		for (final MenuItem menu : menuList) {
			try {
				if (StringsUtils.isNotBlank(menu.getTitle())) {
					final JCheckBox cBox = new JCheckBox(menu.getTitle());
					cBoxs.add(cBox);
					modulePanel.add(cBox);
					for (MenuItem subMenu : menu.getChildren()) {
						if (StringsUtils.isNotBlank(subMenu.getTitle())) {
							final JCheckBox scBox = new JCheckBox("         " + subMenu.getTitle());
							cBoxs.add(scBox);
							modulePanel.add(scBox);
						}
					}
				}
			} catch (final Exception e) {
			}
		}
		main.getContentPane().add(new JScrollPane(modulePanel), BorderLayout.CENTER);

		final JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		main.getContentPane().add(bottom, BorderLayout.SOUTH);
		final JButton ok = new JButton("确定");
		bottom.add(ok);
		final JFileChooser fc = new JFileChooser();
		fc.setSelectedFile(new File("rams.lic"));

		ok.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				final License lic = new License();
				fc.setDialogTitle("请选择目录");
				int result = fc.showDialog(main, "确定");
				if (result != 0)
					return;
				lic.setLicFile(fc.getSelectedFile().getAbsolutePath());
				final java.util.Calendar cal = Calendar.getInstance();

				lic.setCreateDate(cal.getTime());

				cal.add(Calendar.DATE, Integer.parseInt(days.getText()));
				lic.setTimeoutDate(cal.getTime());
				String moudels = "";
				for (final JCheckBox cBox : cBoxs) {
					if (cBox.isSelected()) {
						moudels += cBox.getText() + ",";
					}
				}
				lic.getProps().setProperty("type", typev.getText());
				lic.getProps().setProperty("version", version.getText());
				lic.getProps().setProperty("menus", moudels);
				try {
					lic.save();
				} catch (final IOException e1) {
					e1.printStackTrace();
				}
			}

		});

		final JButton close = new JButton("关闭");
		bottom.add(close);
		close.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				System.exit(-1);
			}

		});

		final JButton open = new JButton("打开");
		bottom.add(open);

		open.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				fc.setDialogTitle("请选择目录");
				int result = fc.showDialog(main, "确定");
				if (result != 0)
					return;

				final License lic = new License(fc.getSelectedFile().getAbsolutePath());
				typev.setText(lic.getProps().getProperty("mac"));
				version.setText(lic.getProps().getProperty("et199"));
				String menus = lic.getProps().getProperty("menus");

				String[] mds = menus.split(",");

				for (final JCheckBox cBox : cBoxs) {
					//					if(cBox.isSelected()) {
					//						moudels += cBox.getText()+",";
					//					}
					cBox.setSelected(false);
					for (String md : mds) {
						if (cBox.getText().equals(md)) {
							cBox.setSelected(true);
							break;
						}
					}
				}

			}

		});

		main.setLocationRelativeTo(null);
		main.setVisible(true);

	}
}