package rs.raf.infview.view.frame;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Map;

import rs.raf.infview.observer.ChangeObserver;
import rs.raf.infview.observer.command.*;
import rs.raf.infview.core.App;
import rs.raf.infview.core.Res;
import rs.raf.infview.state.Context;
import rs.raf.infview.observer.ChangeType;
import rs.raf.infview.observer.ChangeObserverAdapter;
import rs.raf.infview.model.*;
import rs.raf.infview.util.Machine;
import rs.raf.infview.util.ObservableSet;
import rs.raf.infview.util.io.file.AbstractResource;
import rs.raf.infview.util.io.file.FileMatcher;
import rs.raf.infview.view.adapter.action.*;
import rs.raf.infview.util.DelayRunner;
import rs.raf.infview.view.adapter.tab.TabFactory;
import rs.raf.infview.view.adapter.tab.TabManager;
import rs.raf.infview.view.adapter.tab.TabItem;
import rs.raf.infview.view.component.*;
import rs.raf.infview.view.component.Component.Position;
import rs.raf.infview.view.component.Panel.Layout;

public class MainFrame extends FrameTemplate {

    private Root root;
    private ObservableSet<TabItem> tabsTop, tabsBottom;
    private Panel workspace;
    private Component topTabPanel, bottomTabPanel;
    private ChangeObserver<Entity> refreshCallback;
    private int blockSize = App.DEFAULT_BLOCK_SIZE;

    public MainFrame() {
        super(Res.STRINGS.FRAME_MAIN);
    }

	@Override
	protected void configure() {
        setSizeRatio(0.9f);

        root = new Root();
        tabsTop = new ObservableSet<>();
        tabsBottom = new ObservableSet<>();

        refreshCallback = new ChangeObserverAdapter<Entity>() {
            @Override
            public void onChange(ChangeType type, Entity bundle) {
                if(type == ChangeType.REFRESH) {
                    refreshWorkspace(bundle);
                }
            }
        };

        Context.instance().fileMatcher = new FileMatcher();

        initializeWorkspace(true);

        frame.addObserver(new ChangeObserverAdapter() {
            @Override
            public void onChange(ChangeType type) {
                if(type == ChangeType.CLOSE) {
                    CommandQueue.push(new ExitAction());
                }
            }
        });
	}

	@Override
	protected void populate() {
        addMenuBar();
        addToolBar();
        addMainPanel();
        addStatusBar();
	}

	private void addMenuBar() {
        frame.setMenuBar(new MenuBarFactory().getMenuBar(new CompositeActionItem[] {
                new CompositeActionItem(Res.STRINGS.MENU_FILE, 'F', null).children(new CompositeActionItem[] {
                        new CompositeActionItem(Res.STRINGS.MENU_NEW, Res.ICONS.NEW, 'N', new AddAction(root))
                                .separate(),
                        new CompositeActionItem(Res.STRINGS.MENU_OPEN, Res.ICONS.OPEN, 'O', new OpenAction())
                                .separate(),
                        new CompositeActionItem(Res.STRINGS.MENU_SAVE, Res.ICONS.SAVE, 'S', new SaveAction()),
                        new CompositeActionItem(Res.STRINGS.MENU_SAVE_AS, Res.ICONS.SAVE_AS, 'A', new SaveAsAction())
                                .separate(),
                        new CompositeActionItem(Res.STRINGS.MENU_EXIT, Res.ICONS.EXIT, 'Q', new ExitAction())
                }),
                new CompositeActionItem(Res.STRINGS.MENU_EDIT, 'E', null).children(new CompositeActionItem[] {
                        new CompositeActionItem(Res.STRINGS.MENU_UNDO, Res.ICONS.UNDO, 'Z', new UndoAction()),
                        new CompositeActionItem(Res.STRINGS.MENU_REDO, Res.ICONS.REDO, 'Y', new RedoAction())
                                .separate(),
                        new CompositeActionItem(Res.STRINGS.MENU_CUT, Res.ICONS.CUT, 'X', new CutAction()),
                        new CompositeActionItem(Res.STRINGS.MENU_COPY, Res.ICONS.COPY, 'C', new CopyAction()),
                        new CompositeActionItem(Res.STRINGS.MENU_PASTE, Res.ICONS.PASTE, 'V', new PasteAction())
                }).separate(),
                new CompositeActionItem(Res.STRINGS.MENU_SETTINGS, 'P', new SettingsAction()),
                new CompositeActionItem(Res.STRINGS.MENU_ACCOUNT, 'A', null).children(new CompositeActionItem[] {
                        new CompositeActionItem(Res.STRINGS.MENU_LOGOUT, Res.ICONS.LOGOUT, 'L', new LogoutAction(this)),
                }),
                new CompositeActionItem(Res.STRINGS.MENU_HELP, 'H', null).children(new CompositeActionItem[] {
                        new CompositeActionItem(Res.STRINGS.MENU_OFFLINE, Res.ICONS.OFFLINE, 'F', new HelpOfflineAction()),
                        new CompositeActionItem(Res.STRINGS.MENU_ONLINE, Res.ICONS.ONLINE, 'I', new HelpOnlineAction())
                                .separate(),
                        new CompositeActionItem(Res.STRINGS.MENU_ABOUT, Res.ICONS.ABOUT, 'T', new AboutAction())
                })
        }));
    }

    private void addToolBar() {
        frame.addComponent(new ToolBarFactory().getToolBar(new ActionItem[] {
                new ActionItem(Res.STRINGS.MENU_NEW, Res.ICONS.NEW, new AddAction(root)),
                new ActionItem(Res.STRINGS.MENU_OPEN, Res.ICONS.OPEN, new OpenAction()),
                new ActionItem(Res.STRINGS.MENU_SAVE, Res.ICONS.SAVE, new SaveAction()),
                new ActionItem(Res.STRINGS.MENU_EDITOR, Res.ICONS.EDITOR, new EditorAction(root, tabsTop)),
                new ActionItem(Res.STRINGS.MENU_EXIT, Res.ICONS.EXIT, new ExitAction())
        }), Component.Position.TOP);
    }

    private void addMainPanel() {
	    Tree tree = getTree();

        ScrollPanel treePanel = App.UI.getScrollPanel();
        treePanel.addComponent(tree);

        SplitPanel splitMain = App.UI.getSplitPanel();
        splitMain.setLeft(treePanel);
        splitMain.setRight(workspace);
        splitMain.setResizeWeight(0.25);

        frame.addComponent(splitMain, Component.Position.CENTER);
    }

    private Tree getTree() {
        Tree tree = App.UI.getTree();
        tree.setRoot(root);
        tree.addObserver(new ChangeObserverAdapter() {
            @Override
            public void onChange(ChangeType type, Object bundle) {
                if(bundle instanceof Node) {
                    if(type == ChangeType.SELECTION) {
                        Context.instance().tree.addSelectedNode((Node) bundle);
                    }else if(type == ChangeType.DESELECTION) {
                        Context.instance().tree.removeSelectedNode((Node) bundle);
                    }
                }
            }
        });
        tree.setPopupMenu(new PopupMenuFactory().getPopupMenu(new CompositeActionItem[] {
                new CompositeActionItem(Res.STRINGS.MENU_SAVE, new SaveAction()).separate(),
                new CompositeActionItem(Res.STRINGS.MENU_ADD, new AddAction(root, false)),
                new CompositeActionItem(Res.STRINGS.MENU_REMOVE, new RemoveAction(tabsTop)).separate(),
                new CompositeActionItem(Res.STRINGS.MENU_DETAILS, new DetailsAction(tabsTop, tabsBottom, refreshCallback))
        }));

        Context.instance().tree = tree;

        return tree;
    }

    private void initializeWorkspace(boolean firstTime) {
        if(firstTime) {
            workspace = App.UI.getPanel();
            workspace.setLayout(Panel.Layout.EXPAND);

            topTabPanel = getTabPanel(tabsTop);
            bottomTabPanel = getTabPanel(tabsBottom);
        }else {
            workspace.clear();
        }

        Panel top = App.UI.getPanel();
        top.setLayout(Panel.Layout.NORMAL);
        top.addComponent(topTabPanel);

        SplitPanel split = App.UI.getSplitPanel();
        split.setTop(top);
        split.setBottom(bottomTabPanel);
        split.setResizeWeight(0.5);

        workspace.addComponent(split);
    }

    private void refreshWorkspace(Entity entity) {
        Context.instance().entity = entity;
        
        initializeWorkspace(false);

        if(tabsTop.isEmpty() || entity == null) {
            return;
        }
        
        for(TabItem tab : tabsTop) {
            if(tab.getModel().equals(entity)) {
                tab.refresh();
            }
        }
        
        FetchAction fetch = new FetchAction(entity, refreshCallback);
        fetch.setBlockSize(blockSize);

        Label lblTotalRecords = App.UI.getLabel();
        lblTotalRecords.setText(Res.STRINGS.INFO_TOTAL_RECORDS);
        
        TextField tfTotalRecords = App.UI.getTextField();
        tfTotalRecords.setEditable(false);
        tfTotalRecords.setColumns(5);
        tfTotalRecords.setText("?");

        Label lblRecordSize = App.UI.getLabel();
        lblRecordSize.setText(Res.STRINGS.INFO_RECORD_SIZE);
        
        TextField tfRecordSize = App.UI.getTextField();
        tfRecordSize.setEditable(false);
        tfRecordSize.setColumns(5);
        tfRecordSize.setText("?");

        Label lblCurrentBlock = App.UI.getLabel();
        lblCurrentBlock.setText(Res.STRINGS.INFO_CURRENT_BLOCK);

        TextField tfCurrentBlock = App.UI.getTextField();
        tfCurrentBlock.setEditable(false);
        tfCurrentBlock.setColumns(5);
        tfCurrentBlock.setText(String.valueOf(0));

        Label lblBlockSize = App.UI.getLabel();
        lblBlockSize.setText(Res.STRINGS.INFO_BLOCK_SIZE);

        TextField tfBlockSize = App.UI.getTextField();
        tfBlockSize.setEditable(true);
        tfBlockSize.setColumns(5);
        tfBlockSize.setText(String.valueOf(fetch.getBlockSize()));
        
        Button btnApply = new ButtonFactory().getButton(new ActionItem(Res.STRINGS.MENU_APPLY, new Command() {
            @Override
            public void execute() {
                int num;

				try {
					num = Integer.parseInt(tfBlockSize.getText());
				}catch (NumberFormatException e) {
					num = fetch.getBlockSize();
				}

                tfBlockSize.setText(String.valueOf(num));
                
                fetch.setBlockSize(num);
                fetch.setEntity(entity);
                blockSize = num;
            }
        }));
        
        Panel panelBot = App.UI.getPanel();
        panelBot.addComponent(lblBlockSize);
        panelBot.addComponent(tfBlockSize);
        panelBot.addComponent(btnApply);
        panelBot.addComponent(lblCurrentBlock);
        panelBot.addComponent(tfCurrentBlock);
        panelBot.addComponent(lblRecordSize);
        panelBot.addComponent(tfRecordSize);
        panelBot.addComponent(lblTotalRecords);
        panelBot.addComponent(tfTotalRecords);

        Panel panel = App.UI.getPanel();
        panel.setLayout(Layout.EXPAND);
        panel.addComponent(new ToolBarFactory().getToolBar(new ActionItem[] {
                new ActionItem(Res.STRINGS.MENU_CREATE, Res.ICONS.CREATE, new CreateAction(entity, refreshCallback)),
                new ActionItem(Res.STRINGS.MENU_FETCH, Res.ICONS.FETCH, fetch),
                new ActionItem(Res.STRINGS.MENU_UPDATE, Res.ICONS.UPDATE, new UpdateAction(entity, refreshCallback)),
                new ActionItem(Res.STRINGS.MENU_DELETE, Res.ICONS.DELETE, new DeleteAction(entity, refreshCallback)),
                new ActionItem(Res.STRINGS.MENU_SORT, Res.ICONS.SORT, new SortAction(entity, refreshCallback)),
                new ActionItem(Res.STRINGS.MENU_SEARCH, Res.ICONS.SEARCH, new SearchAction(entity, refreshCallback)),
                new ActionItem(Res.STRINGS.MENU_TRASH, Res.ICONS.DELETE, new TrashAction(entity, refreshCallback)),
                new ActionItem(Res.STRINGS.MENU_COUNT, Res.ICONS.COUNT, new CountAction(entity)),
                new ActionItem(Res.STRINGS.MENU_AVERAGE, Res.ICONS.AVERAGE, new AverageAction(entity)),
                new ActionItem(Res.STRINGS.MENU_CONVERT, Res.ICONS.CONVERT, new ConvertAction(entity))
        }, true), Position.TOP);
        
        panel.addComponent(panelBot, Position.LEFT);
        
        workspace.addComponent(panel, Position.TOP);

        AbstractResource file = Context.instance().fileMatcher.get(entity);

        if(file != null) {
            Map<AbstractResource.InfoEntry, Integer> fileInfo = file.getInfo();

            int totalRecords = fileInfo.get(AbstractResource.InfoEntry.TOTAL_RECORDS);
            int recordSize = fileInfo.get(AbstractResource.InfoEntry.RECORD_SIZE);
            long currentBlock;
            try {
            	currentBlock = (fetch.getCurrentPointer(file) / recordSize) / blockSize;
            }catch (ArithmeticException e) {
				currentBlock = 0;
			}

            tfCurrentBlock.setText(String.valueOf(currentBlock));
            tfTotalRecords.setText(String.valueOf(totalRecords));
            tfRecordSize.setText(String.valueOf(recordSize));
        }
    }

    private Component getTabPanel(ObservableSet<TabItem> tabs) {
        TabManager manager = new TabManager();
        manager.addObserver(new ChangeObserverAdapter<TabItem>() {
            @Override
            public void onChange(ChangeType type, TabItem bundle) {
                if(bundle == null) {
                    return;
                }

                Entity entity = (Entity) bundle.getModel();

                if(type == ChangeType.REMOVAL) {
                    CommandQueue.push(new CloseTabAction(entity, tabs));

                    refreshWorkspace(null);
                }else if(type == ChangeType.SELECTION) {
                	if(tabs == tabsBottom) {
                	    return;
                    }

                    tabsBottom.clear();

                    TabFactory tabFactory = new TabFactory(tabsBottom, null);

                    for(Attribute a : entity.getAttributes()) {
                        if(a.isRelation()) {
                            Entity child = a.getRelation().getEntity();

                            if(child != null) {
                                tabsBottom.add(new TabItem(child, tabFactory.getTab(child, true)));
                            }
                        }
                    }
                	
                	((Entity)bundle.getModel()).clearFiltered();

                    refreshWorkspace((Entity) bundle.getModel());
                }
            }
        });

        tabs.addObserver(new ChangeObserverAdapter<TabItem>() {
            @Override
            public void onChange(ChangeType type, TabItem bundle) {
                if(type == ChangeType.ADDITION) {
                    manager.addTab(bundle);

                    if(tabs != tabsBottom)
                    	refreshWorkspace((Entity) bundle.getModel());
                }else if(type == ChangeType.REMOVAL) {
                    manager.removeTab(bundle);

                    Entity entity = null;

                    for(TabItem tab : tabsTop) {
                        entity = (Entity) tab.getModel();
                    }

                    refreshWorkspace(entity);
                }
            }
        });

        
        return manager.getPanel();
    }

    private void addStatusBar() {
	    Label statusTime = App.UI.getLabel();
	    statusTime.setPosition(Component.Position.LEFT);

        new DelayRunner(new ChangeObserverAdapter() {
            @Override
            public void onChange() {
                statusTime.setText(new SimpleDateFormat(Res.FORMATS.DATETIME).format(new Date()));
            }
        }, 1000).run();

        Label commandStatus = App.UI.getLabel();
        commandStatus.setPosition(Component.Position.CENTER);

        CommandQueue.observe(new ChangeObserverAdapter<Command>() {
            @Override
            public void onChange(ChangeType type, Command bundle) {
                if(bundle.hasStatus()) {
                    commandStatus.setText(bundle.getStatus());
                }
            }
        });

        Label statusPlatform = App.UI.getLabel();
        statusPlatform.setPosition(Component.Position.RIGHT);
        statusPlatform.setText(Machine.user() + " @ " + Machine.platform());

        Panel statusBar = App.UI.getPanel();
        statusBar.setLayout(Panel.Layout.SHRINK);
        statusBar.setPadding(0, 10, 2, 10);
        statusBar.addComponent(statusTime);
        statusBar.addComponent(commandStatus);
        statusBar.addComponent(statusPlatform);
        frame.addComponent(statusBar, Component.Position.BOTTOM);
    }
}