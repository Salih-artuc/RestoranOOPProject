package com.restoran.model;

/**
 * Menü abstract sınıfı
 */
public abstract class Menu {
    protected String menuName;
    protected String menuType;

    public Menu(String menuName, String menuType) {
        this.menuName = menuName;
        this.menuType = menuType;
    }

    // Abstract method
    public abstract void displayMenu();

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getMenuType() {
        return menuType;
    }

    public void setMenuType(String menuType) {
        this.menuType = menuType;
    }
}

