package com.aris.admin.shared;

public class WindowProps {

        private String menubar;
        private int location;
        private String resizable;
        private String scrollbars;
        private int status;
        private boolean dependent;
        private int width;
        private int height;

    public WindowProps(String menubar, int location, String resizable, String scrollbars, int status, boolean dependent, int width, int height) {
        this.menubar = menubar;
        this.location = location;
        this.resizable = resizable;
        this.scrollbars = scrollbars;
        this.status = status;
        this.dependent = dependent;
        this.width = width;
        this.height = height;
    }

    public WindowProps() {
        this.menubar = "no";
        this.location = 0;
        this.resizable = "yes";
        this.scrollbars = "yes";
        this.status = 0;
        this.dependent = true;
        this.width = 1200;
        this.height = 720;
    }

    public String getMenubar() {
        return menubar;
    }

    public void setMenubar(String menubar) {
        this.menubar = menubar;
    }

    public int getLocation() {
        return location;
    }

    public void setLocation(int location) {
        this.location = location;
    }

    public String getResizable() {
        return resizable;
    }

    public void setResizable(String resizable) {
        this.resizable = resizable;
    }

    public String getScrollbars() {
        return scrollbars;
    }

    public void setScrollbars(String scrollbars) {
        this.scrollbars = scrollbars;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isDependent() {
        return dependent;
    }

    public void setDependent(boolean dependent) {
        this.dependent = dependent;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
