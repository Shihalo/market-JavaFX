package filonenko.sales.controllers;

import filonenko.sales.apps.CurrentUser;
import filonenko.sales.apps.MenuEventsHandler;
import filonenko.sales.entities.Product;
import filonenko.sales.services.ProductService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.List;

public class Products {
    public MenuItem usersMenu;
    public MenuItem productMenu;
    public Button log;
    public Button profile;

    public TableColumn<Product, String> name;
    public TableColumn<Product, String> firm;
    public TableColumn<Product, Double> unit_price;
    public TableView<Product> table;
    public Button add;
    public MenuItem salesMenu;
    private ObservableList<Product> products = FXCollections.observableArrayList();
    private List<Boolean> selected = new ArrayList<>();

    @FXML
    private void initialize() throws Exception {
        MenuEventsHandler.eventHandlers(usersMenu, productMenu, salesMenu, log, profile);
        name.setSortable(false);
        firm.setSortable(false);
        unit_price.setSortable(false);
        thisEventHandlers();
        name.setCellValueFactory(new PropertyValueFactory<Product, String>("Name"));
        firm.setCellValueFactory(new PropertyValueFactory<Product, String>("Firm"));
        unit_price.setCellValueFactory(new PropertyValueFactory<Product, Double>("Unit_price"));
        table.setMaxHeight(200);
        tableUpdate();

    }

    private void thisEventHandlers() {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem edit = new MenuItem("Изменить");
        edit.setOnAction(contextEvent -> {
            ProductService.editProduct(table.getSelectionModel().getSelectedItem());
            tableUpdate();
        });
        MenuItem delete = new MenuItem("Удалить");
        delete.setOnAction(contextEvent -> {
            ProductService.deleteProduct(table.getSelectionModel().getSelectedItem());
            tableUpdate();
        });

        add.setVisible(false);
        if(CurrentUser.getCurrentUser() != null) {
            contextMenu.getItems().addAll(edit, delete);
            if(CurrentUser.getCurrentUser().getAccess() == 1) { add.setVisible(true);
                add.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                    ProductService.addProduct();
                    tableUpdate();
                }); }
        }
        table.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            contextMenu.hide();
            switch (event.getButton()) {
                case PRIMARY: {
                    for (Node n : table.lookupAll("TableRow")) {
                        if (n instanceof TableRow) {
                            TableRow row = (TableRow) n;
                            if (row.getStyle() != ("-fx-background-color: rgb(152, 251, 152);")) {
                                row.setStyle("-fx-background-color: white;");
                            }
                            if (row.getIndex() == table.getSelectionModel().getFocusedIndex()) {
                                if (!selected.get(row.getIndex())) {
                                    selected.set(row.getIndex(), !selected.get(row.getIndex()));
                                    row.setStyle("-fx-background-color: rgb(152, 251, 152);");
                                } else {
                                    selected.set(row.getIndex(), !selected.get(row.getIndex()));
                                    row.setStyle("-fx-background-color: rgb(135, 206, 235);");
                                }
                            }
                        }
                    }
                    break;
                }
                case SECONDARY: {
                    table.setOnContextMenuRequested(contextEvent -> contextMenu.show(table, event.getScreenX(), event.getScreenY()));
                    break;
                }
            }
        });
    }

    private void tableUpdate() {
        try {
            List<Product> productList = ProductService.getAllProducts();
            products.setAll(productList);
            table.setItems(products);
            table.setPrefHeight(25+productList.size()*25);
            selected.clear();
            for(Product ignored : productList)
                selected.add(false);
            for (Node n : table.lookupAll("TableRow")) {
                if (n instanceof TableRow) {
                    TableRow row = (TableRow) n;
                    row.setStyle("-fx-background-color: white;");
                }
            }
        } catch (Exception ignored) { }
    }
}
