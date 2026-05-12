module cascara.macos.menus {
    requires javafx.base;
    requires transitive javafx.controls;
    requires transitive javafx.graphics;

    requires nsmenufx;
    requires cascara.ui;

    provides io.github.qishr.cascara.ui.menu.SystemMenusService
        with io.github.qishr.cascara.macos.menus.MacosSystemMenus;

}
