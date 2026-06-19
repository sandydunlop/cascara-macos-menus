package io.github.qishr.cascara.macos.menus;

import javafx.scene.control.Menu;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

import de.jangassen.MenuToolkit;
import io.github.qishr.cascara.common.service.ServiceProvider;
import io.github.qishr.cascara.common.util.Properties;
import io.github.qishr.cascara.ui.menu.ObservableMenuFactory;
import io.github.qishr.cascara.ui.menu.ObservableMenuItem;
import io.github.qishr.cascara.ui.menu.SystemMenusService;

public class MacosSystemMenus implements SystemMenusService, ServiceProvider {
    private MenuToolkit tk;
    private Properties capabilities;
    private ObservableMenuItem menuRoot;
    private ObservableMenuItem appMenu;

    Runnable onAbout = null;
    public void setOnAbout(Runnable handler) {this.onAbout = handler;}
    public void onAbout() {if (onAbout != null) {onAbout.run();}}

    Runnable onSettings = null;
    public void setOnSettings(Runnable handler) {this.onSettings = handler;}
    public void onSettings() {if (onSettings != null) {onSettings.run();}}

    Runnable onQuit = null;
    public void setOnQuit(Runnable handler) {this.onQuit = handler;}
    public void onQuit() {if (onQuit != null) {onQuit.run();}}


    public MacosSystemMenus() {
        tk = MenuToolkit.toolkit();
    }

    @Override
    public Properties getServiceProperties() {
        if (capabilities == null) {
            capabilities = new Properties();
            capabilities.set("platform", "macOS");
        }
        return capabilities;
    }

    @Override
    public void setMenuRoot(ObservableMenuItem menuRoot) {
        this.menuRoot = menuRoot;
    }

    @Override
    public void integrate(Stage stage) {
        tk.setMenuBar(stage, menuRoot.getMenuBar());
        tk.setApplicationMenu((Menu)appMenu.getMenuItem());
    }

    /// Builds the application menu for the macOS menu bar.
    @Override
    public ObservableMenuItem buildAppMenu(String appName) {
        appMenu = menuRoot.addMenu("app", appName);

        appMenu.addMenuItem("about", "About...")
            .setOnChoose(() -> onAbout());

        appMenu.addSeparator();

        appMenu.addMenuItem("settings", "Settings...")
            .setAccelerator(new KeyCodeCombination(KeyCode.COMMA, KeyCombination.META_DOWN))
            .setOnChoose(() -> onSettings());

        appMenu.addSeparator();

        appMenu.addMenuItem(
            "hide-app", "Hide App",
            tk.createHideMenuItem(appName)
        );

        appMenu.addMenuItem(
            "hide-others", "Hide Others",
            tk.createHideOthersMenuItem()
        );

        appMenu.addMenuItem(
            "show-all", "Show All",
            tk.createUnhideAllMenuItem()
        );

        appMenu.addSeparator();

        appMenu.addMenuItem(
            "quit", "Quit",
            tk.createQuitMenuItem(appName)
        ).setOnChoose(() -> onQuit());

        return appMenu;
    }

    @Override
    public ObservableMenuItem buildWindowMenu() {
        ObservableMenuItem windowMenu = ObservableMenuFactory.createMenu("window", "Window");

        ObservableMenuItem minimize = ObservableMenuFactory.createMenuItem(
            "minimize", "Minimize",
            tk.createMinimizeMenuItem()
        );

        ObservableMenuItem zoom = ObservableMenuFactory.createMenuItem(
            "zoom", "Zoom",
            tk.createZoomMenuItem()
        );

        ObservableMenuItem bringAllToFront = ObservableMenuFactory.createMenuItem(
            "bring-all-to-front", "Bring All To Fron",
            tk.createBringAllToFrontItem()
        );

        windowMenu.getChildren().addAll(
            minimize, zoom, ObservableMenuItem.SEPARATOR, bringAllToFront
        );

        return windowMenu;
    }
}
