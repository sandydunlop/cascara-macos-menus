// # License & Terms
//
// This file is part of **Cascara**.
//
// **Cascara** is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program. If not, see <https://www.gnu.org/licenses/>.
//
// ---
//
// ## Special Runtime Exception
//
// As a special exception, the copyright holders of this library give you
// permission to link this library with independent modules to produce an
// executable, regardless of the license terms of these independent modules,
// and to copy and distribute the resulting executable under terms of your
// choice, provided that you also meet, for each linked independent module,
// the terms and conditions of the license of that module.
//
// An independent module is a module which is not derived from or based on
// this library. If you modify this library, you may extend this exception
// to your version of the library, but you are not obligated to do so. If
// you do not wish to do so, delete this exception statement from your
// version.


package io.github.qishr.cascara.macos.menus;

import javafx.scene.control.Menu;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

import de.jangassen.MenuToolkit;
import io.github.qishr.cascara.common.service.ServiceProvider;
import io.github.qishr.cascara.common.property.Properties;
import io.github.qishr.cascara.ui.menu.ObservableMenuFactory;
import io.github.qishr.cascara.ui.menu.ObservableMenuItem;
import io.github.qishr.cascara.ui.menu.SystemMenusService;

public class MacosSystemMenus implements SystemMenusService {
    private MenuToolkit tk;
    private Properties properties;
    private ObservableMenuItem menuRoot;
    private ObservableMenuItem appMenu;
    private Runnable onAbout;
    private Runnable onSettings;
    private Runnable onQuit;

    public MacosSystemMenus() {
        tk = MenuToolkit.toolkit();
    }

    @Override
    public Properties getServiceProperties() {
        if (properties == null) {
            properties = new Properties();
            properties.set("platform", "macOS");
        }
        return properties;
    }

    @Override
    public void setOnAbout(Runnable handler) {this.onAbout = handler;}

    public void onAbout() {if (onAbout != null) {onAbout.run();}}

    @Override
    public void setOnSettings(Runnable handler) {this.onSettings = handler;}

    public void onSettings() {if (onSettings != null) {onSettings.run();}}

    @Override
    public void setOnQuit(Runnable handler) {this.onQuit = handler;}

    public void onQuit() {if (onQuit != null) {onQuit.run();}}

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
