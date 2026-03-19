package io.github.qishr.cascara.macos.menus;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

import de.jangassen.MenuToolkit;
import io.github.qishr.cascara.ui.platform.SystemMenusService;

public class MacosSystemMenus implements SystemMenusService {
    MenuToolkit tk;
    String appName;

    MenuOptionHandler onAbout = null;
    public void setOnAbout(MenuOptionHandler handler) {this.onAbout = handler;}
    public void onAbout(MenuItem i) {if (onAbout != null) {onAbout.onMenuOption(i);}}

    MenuOptionHandler onSettings = null;
    public void setOnSettings(MenuOptionHandler handler) {this.onSettings = handler;}
    public void onSettings(MenuItem i) {if (onSettings != null) {onSettings.onMenuOption(i);}}

    MenuOptionHandler onQuit = null;
    public void setOnQuit(MenuOptionHandler handler) {this.onQuit = handler;}
    public void onQuit(MenuItem i) {if (onQuit != null) {onQuit.onMenuOption(i);}}


    public MacosSystemMenus() {
        tk = MenuToolkit.toolkit();
    }


    @Override
    public void setAppName(String appName) {
        this.appName = appName;
    }


    @Override
    public void integrate(Stage stage, MenuBar menuBar, Menu appMenu) {
        tk.setMenuBar(stage, menuBar);
        // Set the application menu to the custom app menu directly
        tk.setApplicationMenu(appMenu);
    }


    @Override
    public Menu buildAppMenu() {
        Menu appMenu = new Menu(appName);

        MenuItem aboutItem = new MenuItem("About...");
        aboutItem.setOnAction(event -> {
            onAbout(aboutItem);
        });

        SeparatorMenuItem settingsGroupSep = new SeparatorMenuItem();

        MenuItem settingsItem = new MenuItem("Settings...");
        settingsItem.setAccelerator(new KeyCodeCombination(KeyCode.COMMA, KeyCombination.META_DOWN));
        settingsItem.setOnAction(event -> {
            onSettings(settingsItem);
        });

        SeparatorMenuItem windowGroupSep = new SeparatorMenuItem();

        MenuItem hideCascara = tk.createHideMenuItem(appName);
        MenuItem hideOthers = tk.createHideOthersMenuItem();
        MenuItem showAll = tk.createUnhideAllMenuItem();

        SeparatorMenuItem quitGroupSep = new SeparatorMenuItem();

        MenuItem quitItem = tk.createQuitMenuItem(appName);
        quitItem.setOnAction(event -> {
            onQuit(quitItem);
        });

        appMenu.getItems().addAll(
            aboutItem,

            settingsGroupSep,
            settingsItem,
            // new Menu("App Submenu"),

            windowGroupSep,
            hideCascara,
            hideOthers,
            showAll,

            quitGroupSep,
            quitItem
        );

        return appMenu;
    }

    @Override
    public Menu buildWindowMenu() {
        Menu windowMenu = new Menu("Window");

        MenuItem minimize = tk.createMinimizeMenuItem();
        MenuItem zoom = tk.createZoomMenuItem();

        SeparatorMenuItem separator = new SeparatorMenuItem();

        MenuItem bringAllToFront = tk.createBringAllToFrontItem();

        windowMenu.getItems().addAll(minimize, zoom, separator, bringAllToFront);

        tk.autoAddWindowMenuItems(windowMenu);

        return windowMenu;
    }
}
