package com.catapult.gui;

import com.catapult.listener.AbstractKeyListener;
import com.catapult.listener.GlobalScreenManager;
import com.catapult.managers.OsManager;
import com.catapult.managers.OsManagerFactory;
import com.catapult.monitor.Monitor;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;
import java.util.Optional;

public class OptionsGui {
  private final static Logger LOG = LoggerFactory.getLogger(OptionsGui.class);
  private final OsManager osManager = OsManagerFactory.getOsManager();

  private final JFrame gui;

  public OptionsGui() {
    gui = new JFrame("Catapult");
    gui.setType(Window.Type.UTILITY);
    gui.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
    gui.getContentPane().add(getListenersList());
    gui.pack();
    gui.setAlwaysOnTop(true);
  }

  public void show() {
    if (!gui.isVisible()) {
      Optional<Monitor> monitorMaybe = osManager.getMonitorWithMouse();
      if (monitorMaybe.isEmpty()) {
        throw new IllegalStateException("Could not find monitor with mouse");
      }

      Monitor monitor = monitorMaybe.get();
      LOG.info("Displaying GUI at monitor {}", monitor.getDisplayableMonitorNumber());

      Rectangle bounds = monitor.getBounds();

      gui.setLocationRelativeTo(new JFrame(monitor.getConfiguration()));
      gui.setSize(bounds.width / 3, bounds.height / 3);
      gui.setVisible(true);
    }
  }

  public void close() {
    LOG.info("Closing Catapult GUI");
    gui.dispose();
  }

  private JList<AbstractKeyListener> getListenersList() {
    JList<AbstractKeyListener> list = new JList<>(
        GlobalScreenManager.getListeners().toArray(AbstractKeyListener[]::new)
    );
    list.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
    ((DefaultListCellRenderer)list.getCellRenderer()).setHorizontalAlignment(JLabel.CENTER);

    list.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        AbstractKeyListener listener = list.getSelectedValue();
        LOG.info("Clicked listener {}", listener);

        Map<Integer, Boolean> isActivationForEventId = listener.getIsActivationForEventId();
        for (Map.Entry<Integer, Boolean> entry : isActivationForEventId.entrySet()) {
          String eventKey = NativeKeyEvent.getKeyText(entry.getKey());
          LOG.info("Found event key is {} with isActivationKey {}", eventKey, entry.getValue());
        }
      }
    });

    return list;
  }
}
