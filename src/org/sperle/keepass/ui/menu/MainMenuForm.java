/*
    Copyright (c) 2009-2010 Christoph Sperle <keepassmobile@gmail.com>
    
    This file is part of KeePassMobile.

    KeePassMobile is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    KeePassMobile is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with KeePassMobile.  If not, see <http://www.gnu.org/licenses/>.

*/

package org.sperle.keepass.ui.menu;

import org.sperle.keepass.ui.KeePassMobile;
import org.sperle.keepass.ui.form.AboutForm;
import org.sperle.keepass.ui.form.Forms;
import org.sperle.keepass.ui.form.KeePassMobileForm;
import org.sperle.keepass.ui.form.PreferencesForm;
import org.sperle.keepass.ui.i18n.Messages;
import org.sperle.keepass.ui.icon.Icons;

import com.sun.lwuit.Command;
import com.sun.lwuit.List;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.layouts.BorderLayout;
import com.sun.lwuit.list.DefaultListModel;
import com.sun.lwuit.util.Log;

public class MainMenuForm extends KeePassMobileForm {
    private List mainMenu;
    private Command defaultCommand;

    public MainMenuForm(final KeePassMobile app, MenuItem[] menuItems) {
        super(app, KeePassMobile.NAME + " " + KeePassMobile.VERSION);
        this.getTitleComponent().setIcon(Icons.getKpmIcon());
        
        setLayout(new BorderLayout());
        setScrollable(false);
        
        app.getCommandManager().addCommands(this, createCommands(), defaultCommand);
        
        mainMenu = new List(menuItems);
        mainMenu.setListCellRenderer(new MainMenuListCellRenderer(app.isFastUI()));
        mainMenu.setOrientation(List.VERTICAL);
        if(!app.isFastUI()) mainMenu.setSmoothScrolling(true);
        else mainMenu.setSmoothScrolling(false);
        mainMenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                MenuItem selectedSource = (MenuItem) mainMenu.getSelectedItem();
                if (selectedSource != null) {
                    selectedSource.choosen();
                }
            }
        });
        addComponent(BorderLayout.CENTER, mainMenu);
    }

    private Command[] createCommands() {
        Command[] commands = new Command[5];
        commands[0] = new Command(Messages.get("help")) {
            public void actionPerformed(ActionEvent evt) {
                Forms.showHelp(Messages.get("mainmenu_help"));
            }
        };
        commands[1] = new Command(Messages.get("preferences")) {
            public void actionPerformed(ActionEvent evt) {
                new PreferencesForm(app).show();
            }
        };
        commands[2] = new Command(Messages.get("show_log")) {
            public void actionPerformed(ActionEvent evt) {
                Forms.showLog(Log.getLogContent());
            }
        };
        commands[3] = new Command(Messages.get("about")) {
            public void actionPerformed(ActionEvent evt) {
                new AboutForm(app).show();
            }
        };
        commands[4] = new Command(Messages.get("exit")) {
            public void actionPerformed(ActionEvent evt) {
                app.exit();
            }
        };
        defaultCommand = commands[4]; // exit
        return commands;
    }
    
    protected void goBack() {
        // do nothing
    }
    
    public void refresh(MenuItem[] menuItems) {
        mainMenu.setModel(new DefaultListModel(menuItems));
    }
}
