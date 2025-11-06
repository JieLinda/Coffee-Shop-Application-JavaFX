package com.example.pboproject.controllers;

import com.example.pboproject.CashierApp;
import com.example.pboproject.beans.*;
import com.example.pboproject.dao.OrderDao;
import com.example.pboproject.utils.ConnectionManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.Size;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;


public class OrderController {
    @FXML
    private Button btnaddMember;
    @FXML
    private Button btnaddorder;
    @FXML
    private Button btndelete;
    @FXML
    private Button btnedit;
    @FXML
    private Button btnnext;
    @FXML
    private Button btnSave;
    @FXML
    private TableColumn<order, String> colid;
    @FXML
    private TableColumn<order, Double> colprice;
    @FXML
    private TableColumn<order, String> colproductname;
    @FXML
    private TableColumn<order, Integer> colqty;
    @FXML
    private TableColumn<order, String> colrequest;
    @FXML
    private TableColumn<order, Size> colsize;
    @FXML
    private TableColumn<order, String> coltype;
    @FXML
    private ListView<String> listName;
    @FXML
    private ListView<String> listSize;
    @FXML
    private ListView<String> listType;
    @FXML
    private TextField autotype;
    @FXML
    private TextField autoname;
    @FXML
    private TextField autosize;
    @FXML
    private Pane paneorderlist;
    @FXML
    private Spinner<Integer> spinnerqty;
    @FXML
    private TableView<order> table;
    @FXML
    private TextField fieldRequest;
    @FXML
    private TextField autochange;
    @FXML
    private TextField autototal;
    @FXML
    private TextField autototalQty;
    @FXML
    private TextField autototaltransaction;
    @FXML
    private Button btnEND;
    @FXML
    private Button btncancel;
    @FXML
    private Button btnsavePayment;
    @FXML
    private CheckBox checkMember;
    @FXML
    private CheckBox checkUsePoint;
    @FXML
    private TableColumn<Discount, Double> coldiscount;
    @FXML
    private TableColumn<Discount, String> colpaymentmethod;
    @FXML
    private TableColumn<Discount, String> colproduct;
    @FXML
    private TableColumn<order, String> colidpayment;
    @FXML
    private TableColumn<order, Double> colpricepayment;
    @FXML
    private TableColumn<order, String> colproductnamepayment;
    @FXML
    private TableColumn<order, Integer> colqtypayment;
    @FXML
    private TableColumn<order, Size> colsizepayment;
    @FXML
    private TableColumn<order, Double> coltotalprice;
    //qty * price
    @FXML
    private ComboBox<String> combopaymentmethod;
    @FXML
    private TextField fieldcashier;
    @FXML
    private TextField fieldcustName;
    @FXML
    private TextField fieldmemberID;
    @FXML
    private TextField fieldpoinGain;
    @FXML
    private TextField fieldpayment;
    @FXML
    private TextField fieldAvlPoints;
    @FXML
    private TableView<Discount> tablediscount;
    @FXML
    private TableView<order> tableorders;

    private static ObservableList<PaymentMethod> paymentMethods;
    private static final ObservableList<String> combomethod = FXCollections.observableArrayList() ;
    private ObservableList<Discount> discounts ;
    ObservableList<Discount> appliedDisc ;
    private static final Transaction transaction = new Transaction();
    private static Double totalPrice;
    private static Double totalTransaction;
    private static Integer totalItem;
    private static Double totaldisc = 0.0;
    private String[]  pMethod_Bank = new String[2];
    private static int pMethodID ;
    private static Connection con ;
    private static String id ;
    private static String priceID ;
    private static double price;
    private static final ObservableList<order> data = FXCollections.observableArrayList();
    private final SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000, 0);
    private static boolean next;
    private static boolean end = false;

    @FXML
    public void initialize() throws SQLException {
        totalPrice = 0.0;
        totalItem = 0;
            if (listType!=null) {
                next = false;
                listType.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
                listName.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
                listSize.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
                loadComboType();
                listType.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        autotype.setText(newValue);  // Set TextField dengan nilai yang dipilih
                        try {
                            loadComboName();  // Muat data berdasarkan pilihan baru
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                });
                listName.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        autoname.setText(newValue);  // Set TextField dengan nilai yang dipilih
                        try {
                            loadComboSize();  // Muat data berdasarkan pilihan baru
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                });
                listSize.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        autosize.setText(newValue);  // Set TextField dengan nilai yang dipilih
                    }
                });
            }
            if (table != null && !next) {
                table.setItems(data);
                table.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
                colid.setCellValueFactory(new PropertyValueFactory<>("priceid"));
                colsize.setCellValueFactory(new PropertyValueFactory<>("size"));
                colprice.setCellValueFactory(new PropertyValueFactory<>("price"));
                //transaction_product_details
                colqty.setCellValueFactory(new PropertyValueFactory<>("qty"));
                colrequest.setCellValueFactory(new PropertyValueFactory<>("request"));
                //menu_list
                colproductname.setCellValueFactory(new PropertyValueFactory<>("productName"));
                coltype.setCellValueFactory(new PropertyValueFactory<>("type"));
                spinnerqty.setValueFactory(valueFactory);

            }

            //PAYMENT SCENE

        for (order o: data) {
            totalItem+=o.getQty();
            totalPrice+=o.getTotalprice();
        }
        if (tableorders!= null&&!data.isEmpty() && next) {
            if (paymentMethods==null) {
                loadPaymentMethod();
            }
            if (combopaymentmethod.getItems().isEmpty()||combopaymentmethod.getItems()==null){
                combopaymentmethod.setItems(combomethod);
            }
//
            tableorders.setItems(data);
            autototal.setText(String.valueOf(totalPrice));
            autototalQty.setText(String.valueOf(totalItem));
            totalTransaction =totalPrice;
            autototaltransaction.setText(String.valueOf(totalTransaction));
            transaction.setTotalPrice(totalPrice);
            colidpayment.setCellValueFactory(new PropertyValueFactory<>("priceid"));
            colproductnamepayment.setCellValueFactory(new PropertyValueFactory<>("productName"));
            colsizepayment.setCellValueFactory(new PropertyValueFactory<>("size"));
            colpricepayment.setCellValueFactory(new PropertyValueFactory<>("price"));
            colqtypayment.setCellValueFactory(new PropertyValueFactory<>("qty"));
            coltotalprice.setCellValueFactory(new PropertyValueFactory<>("totalprice"));
        }

    }
    @FXML
    public void next(ActionEvent event) {
        if (!data.isEmpty()) {
            next = true;
            Stage stage = (Stage) btnnext.getScene().getWindow();
            try {
                Parent root = CashierApp.loadScene("payment.fxml");
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @FXML
    public void addMember() {
        Stage stage = (Stage) btnaddMember.getScene().getWindow();
        try {
            Parent root = CashierApp.loadScene("formTableMember.fxml");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadComboType() throws SQLException {
        ObservableList<String> types = null;

        String id;
        try {
            con = ConnectionManager.getConnection();
            types = FXCollections.observableArrayList(OrderDao.getAllColumn(con, "product_type", "menu_list", String.class));

        } catch (Exception e) {
            e.printStackTrace();
        }
        listType.setItems(types);
    }

    private void loadComboName() throws SQLException {
            ObservableList<String> names = null;
            try {
                con = ConnectionManager.getConnection();

            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                names = FXCollections.observableArrayList(OrderDao.getWhere(con, "product_name", "menu_list", "product_type", autotype.getText(), String.class));
            } catch (Exception e){
                e.printStackTrace();
            }
        listName.setItems(names);
    }

    private void loadComboSize() throws SQLException {
            ObservableList<String> sizes = null;
            getProductID();
            if (id != null) {
                try {
                    con = ConnectionManager.getConnection();
                    sizes = FXCollections.observableArrayList(OrderDao.getWhere(con, "size", "menu_price_list", "product_id", id, String.class));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        listSize.setItems(sizes);
    }

    @FXML
    public void addOrder(ActionEvent event) {
        if (spinnerqty.getValue() != 0) {
            getProductID();
            getPriceID();
            getPrice();
            order o = new order();
            o.setPriceid(priceID);
            o.setProductName(autoname.getText());
            o.setType(autotype.getText());
            o.setSize(com.example.pboproject.beans.Sizes.valueOf(autosize.getText()));
            o.setPrice(price);
            if (fieldRequest.getText().isEmpty()||fieldRequest.getText().equalsIgnoreCase("")){
                o.setRequest("-");
            } else {
                o.setRequest(fieldRequest.getText());
            }
            o.setQty(spinnerqty.getValue());
            o.setTotalprice(price * spinnerqty.getValue());
            data.add(o);
            clearfields();
            table.setItems(data);
        }
        next = false;
    }

    @FXML
    public void deleteList(ActionEvent event) {
        order selectedOrder = table.getSelectionModel().getSelectedItem();
        if (selectedOrder != null) {
            transaction.setTotalPrice(totalPrice);
            transaction.setTotalitem(totalItem);
            data.remove(selectedOrder);
            clearfields();
        }
    }

    @FXML
    public void editSelectedRow(ActionEvent event) {
        order selectedItem = table.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            autotype.setText(selectedItem.getType());
            autoname.setText(selectedItem.getProductName());
            autosize.setText(String.valueOf(selectedItem.getSize()));
            fieldRequest.setText(selectedItem.getRequest());
            spinnerqty.getValueFactory().setValue(selectedItem.getQty());
        }
    }

    @FXML
    public void save(ActionEvent event) {
        order editedOrder = table.getSelectionModel().getSelectedItem();
        int idx;
        if (editedOrder != null) {
            getProductID(); //mengisi product ID
            getPriceID(); //mengisi price id
            getPrice(); //mengisi price
            editedOrder.setPriceid(priceID);
            editedOrder.setPrice(price);
            editedOrder.setType(autotype.getText());
            editedOrder.setProductName(autoname.getText());
            editedOrder.setSize(Sizes.valueOf(autosize.getText()));
            editedOrder.setRequest(fieldRequest.getText());
            editedOrder.setQty(spinnerqty.getValue());
            editedOrder.setTotalprice(price*spinnerqty.getValue());
            clearfields();
            table.refresh();
        }
    }

    private void getProductID(){
        try {
            con = ConnectionManager.getConnection();
            id = OrderDao.getValueById(con, "product_id", "menu_list", "product_name", autoname.getText(), String.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void getPriceID (){
        if (id!=null ) {
            try {
                con = ConnectionManager.getConnection();
                //dapatkan price ID
                priceID = OrderDao.getValueByCombination(con, "price_id", "menu_price_list", "product_id", id, "size", autosize.getText(), String.class);
                if (priceID != null) {
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private void getPrice (){
        if (priceID!=null) {
            try {
                con = ConnectionManager.getConnection();
                price = OrderDao.getValueById(con, "price", "menu_price_list", "price_id", priceID, Double.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    private void clearfields(){
        if (autosize != null) {
            autosize.clear();
        }
        if (autoname != null) {
            autoname.clear(); // Ensure autosize is initialized before calling clear()
        }
        if (autotype!=null) {
            autotype.clear();
        }
        if (fieldRequest!=null) {
            fieldRequest.clear();
        }
        if (spinnerqty != null) {
            spinnerqty.setValueFactory(valueFactory);
            valueFactory.setValue(0);
        }
        this.id = null;
        this.priceID = null;
        this.price=0;
        if (table!=null) {
            table.getSelectionModel().clearSelection();
        }
    }

    private String generateID(String date) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String formattedDate = null;
        String noUrutFormatted = null;

        try {
            LocalDate currentDate = LocalDate.parse(date, dateFormatter);
            formattedDate = currentDate.format(dateFormatter);

            con = ConnectionManager.getConnection();
            int noUrut = OrderDao.getDateID(con, formattedDate);
            noUrutFormatted = String.format("%02d", noUrut);
        } catch (DateTimeParseException e) {
            System.err.println("Error parsing date: " + date);
            e.printStackTrace();
            // Handle the parsing error as needed
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database related errors
        }

        // Ensure noUrutFormatted is initialized to a default value if not set
        if (noUrutFormatted == null) {
            noUrutFormatted = ""; // or any other default value
        }

        // Return the generated ID
        return (formattedDate != null ? formattedDate : "") + noUrutFormatted;
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.show();
    }
    @FXML
    public void saveTransaction(ActionEvent event){
        totaldisc = 0.0;
        //mengupdate cust.name dan mengisi points gain pada checked Member
        Membership m = new Membership();
        if (!fieldcustName.getText().isEmpty()||!fieldcustName.getText().equalsIgnoreCase("")) {
            if (checkMember.isSelected()) {
                if (!fieldmemberID.getText().isEmpty() || !fieldmemberID.getText().equalsIgnoreCase("")&&checkMember.isSelected()) {
                    try {
                        m = getMember(Integer.parseInt(fieldmemberID.getText()));
                    } catch (NumberFormatException e) {
                        showAlert("Invalid Input", "Please enter a valid Member ID.");
                        checkMember.setSelected(false);
                        return;
                    }
                    if (m != null && m.getMemberName() != null) {
                        transaction.setCustname(m.getMemberName());
                        fieldcustName.setText(m.getMemberName());
                        int points = (int) (totalPrice / 10000.0);
                        transaction.setPointgain(points);
                        int updatePoint = m.getTotalPoints() + points;
                        fieldpoinGain.setText(String.valueOf(points));
                        transaction.setMember_id(m.getMemberId());

                        transaction.setPoint_disc(m.getTotalPoints());
                        fieldAvlPoints.setText(String.valueOf(m.getTotalPoints()));
                    } else {
                        showAlert("ID not found!", "Invalid Member ID.");
                        fieldmemberID.setText("");
                        checkUsePoint.setSelected(false);
                        checkMember.setSelected(false);
                        fieldpoinGain.clear();
                        fieldAvlPoints.clear();
                    }
                } else {
                    showAlert("Member ID field is EMPTY", "Input member ID!");
                    checkMember.setSelected(false);
                }
            } else {
                transaction.setCustname(fieldcustName.getText());
                System.out.println("Memasangkan cust name yang non member");
            }
        } else {
            showAlert("Customer Name is EMPTY!","Input customer name first!");
        }



        transaction.setTotalitem(totalItem);
        transaction.setTotalPrice(totalPrice); //total harga seluruh orderan

        totalTransaction = totalPrice;
        System.out.println(totalPrice+" total price di sini");
        pMethod_Bank = combopaymentmethod.getValue().split(" - ");
        //Mendapatkan ID paymentmethod yang sedang diterapkan pada comboPaymentMehtod
        for (PaymentMethod p : paymentMethods) {
            if (pMethod_Bank[0].equalsIgnoreCase(p.getPaymentMethod())&&pMethod_Bank[1].equalsIgnoreCase(p.getBank())||pMethod_Bank[0].equalsIgnoreCase(p.getPaymentMethod())&&pMethod_Bank[1].equalsIgnoreCase("null")){
                pMethodID = p.getPaymentId();
            }
        }
        loadDiscount();
        ArrayList<Discount> potonganTotalTransaction = new ArrayList<>(); //untuk yang tidak terpaired dgn product detail
        for (Discount d: appliedDisc) {
            boolean applied = false; //anggapan semua diskon blm paired dengan trans product detail
            for (order o: data) {
                if ((o.getDicount_id() == d.getId())) {
                    applied = true;
                    totalTransaction -= o.getTotaldisc();
                    totaldisc += o.getTotaldisc();
                    System.out.println("total transaksi saat ini = " +totalTransaction );
                    System.out.println("total diskon saat ini = " +totaldisc );
                    System.out.println("Pengurangan total transaksi dengan diskon yg paired: "+o.getTotaldisc());
                }
            }
            if (!applied && d.getRequiredProduct().equalsIgnoreCase("")&&d.getRequiredType().equalsIgnoreCase("")) {
                potonganTotalTransaction.add(d);
                System.out.println("1 diskon yg tidak paired dicatat");
                System.out.println(d.getDisc()+" "+d.getPayment_id()+" "+d.getRequiredProduct()+" "+d.getRequiredType());

            }
        }
        //untuk diskon yg tidak terpaired pada product detail, terapkan pada setiap item (all item)
        if (!potonganTotalTransaction.isEmpty()){
            for (Discount d: potonganTotalTransaction) {
                for (order o: data) {
                    totalTransaction -= (o.getTotalprice()*d.getDisc());
                    totaldisc += (o.getTotalprice()*d.getDisc());
                    System.out.println("total transaksi saat ini#2 = " +totalTransaction );
                    System.out.println("total diskon saat ini#2 = " +totaldisc );
                    System.out.println("Pengurangan total transak degan diskon yang all item: "+(o.getTotalprice()*d.getDisc()));
                }
            }
        }


        if (checkUsePoint.isSelected() && checkMember.isSelected()&&m.getMemberName()!= null&&m.getTotalPoints()>0){
            totalTransaction -= m.getTotalPoints();
            transaction.setPoint_disc(m.getTotalPoints());
            System.out.println("Pengurangan total transak dengan poin member: " +m.getTotalPoints());
//
        }

        transaction.setTotalDisc(totaldisc); //total potongan yang didapat pelanggan dari ist discount yang ada
        transaction.setTotalTransaction(totalTransaction); // total price setelah dikurangi total transaction
        autototaltransaction.setText(String.valueOf(totalTransaction));

        transaction.setCashier(fieldcashier.getText());
        //mengisi Payment_method transaction dengan payment_id
        transaction.setPayment_method(pMethodID);

        pMethod_Bank = combopaymentmethod.getValue().split(" - ");
        System.out.println(pMethod_Bank[0]+" "+pMethodID);
        //UNTUK PEMBAYARAN CASH : NOMINAL DIBAYAR - TOTAL TRANSAKSI = CHANGE
        if (pMethod_Bank[0].equalsIgnoreCase("cash")){
            if (fieldpayment.getText().equalsIgnoreCase("")){
                showAlert("Input Payment!", "Field Payment for Cash is EMPTY");
                System.out.println("Pemeriksaaan kelengkapan cash");
            } else {
                String paymentText = fieldpayment.getText();
                double paymentValue;
                try {
                    paymentValue = Double.parseDouble(paymentText);
                    transaction.setPayment(paymentValue);
                    autochange.setText(String.valueOf(Double.parseDouble(fieldpayment.getText()) - totalTransaction));
                    transaction.setChange(Double.parseDouble(fieldpayment.getText()) - totalTransaction);
                    if (paymentValue<totalTransaction){
                        showAlert("Not Enough Payment","Cash that's paid is less than total transaction");
                        fieldpayment.clear();
                        autochange.clear();
                    }
                    System.out.println("pelaksanaan sistem cash");
                } catch (NumberFormatException e) {
                    showAlert("Invalid Payment Input", "Payment should be a nominal");
                    fieldpayment.clear();
                    autochange.clear();
                }
            }
        } else {
            transaction.setPayment(0.0);
            transaction.setChange(0.0);
        }
        tablediscount.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        System.out.println("load diskon ke table");
        coldiscount.setCellValueFactory(new PropertyValueFactory<>("disc"));
        colpaymentmethod.setCellValueFactory(new PropertyValueFactory<>("pmethod"));
        colproduct.setCellValueFactory(cellData -> {
            Discount discount = cellData.getValue();
            String combinedValue = discount.getRequiredType() + " " + discount.getRequiredProduct();
            return new SimpleStringProperty(combinedValue);
        });
        autototaltransaction.setText(String.valueOf(totalTransaction));

        //GENERATE TRANSACTION NUMBER
        LocalDate currentDate = LocalDate.now();
        String[] date = String.valueOf(currentDate).split("-");
        transaction.setNumber(generateID(date[0] + date[1] + date[2]));
        System.out.println(generateID(date[0] + date[1] + date[2]));
    }

    @FXML
    public void endTransac (ActionEvent event){
        if (fieldcashier.getText().equalsIgnoreCase("")||fieldcashier.getText().isEmpty()){
            showAlert("Cashier is EMPTY", "Input a cashier name");
            System.out.println("Cashier valid");
        } else {
            if (checkUsePoint.isSelected()){
                int updatedPoints;
                int availablePoints = Integer.parseInt(fieldAvlPoints.getText());
                if (availablePoints<=totalTransaction){
                    updatedPoints = 0;
                } else {
                    updatedPoints = (int) (availablePoints-totalTransaction);
                }
                try {
                    OrderDao.updatePoints(Integer.parseInt(fieldmemberID.getText()),updatedPoints);
                    System.out.println("Subtract point success");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (checkMember.isSelected()) {
                Membership m;
                int updatedPoints = Integer.parseInt(fieldpoinGain.getText());
                try {
                    m = getMember(Integer.parseInt(fieldmemberID.getText()));
                } catch (NumberFormatException e) {
                    checkMember.setSelected(false);
                    System.out.println("Reload the member");
                    return;
                }
                if (m != null && m.getMemberName() != null) {
                    updatedPoints += m.getTotalPoints();
                    try {
                        OrderDao.updatePoints(m.getMemberId(), updatedPoints);
                        System.out.println("Plus point success");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            try {
                con = ConnectionManager.getConnection();
                OrderDao.EndTransac(con, transaction);
                System.out.println("Transaction's successfully sent into DB");
                end = true;//jika berhasil menyimpan transaksi
            } catch (Exception e) {
                end = false;
                e.printStackTrace();
                System.out.println("Gagal menyimpan transaksi");
            }

            for (order o : data) {
                o.setTransactionNum(transaction.getNumber());
                System.out.println("Memasangkan no transaksi ke detail orders");
            }
            if (end) { //jika transaksi berhasil tersimpan
                clearFieldsPayment();
                System.out.println("Clear payment field");
                try {
                    //simpan orders ke transaction product details
                    con = ConnectionManager.getConnection();
                    OrderDao.saveOrders(con, data);
                    System.out.println("Detail orders berhasil disimpan ke DB");
                    data.clear();
                    discounts.clear();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // Back to formTable_order.fxml
                Stage stage = (Stage) btnEND.getScene().getWindow();
                try {
                    Parent root = CashierApp.loadScene("formTabel_order.fxml");
                    System.out.println("pindah halaman kembali ke form order");
                    stage.setScene(new Scene(root));
                    stage.show();
                    next = false;
                    end = false;
                    clearFieldsPayment();
                    clearfields();
                    checkUsePoint.setSelected(false);
                    fieldAvlPoints.clear();
                    transaction.reset();
                    System.out.println("clear");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private void loadDiscount(){
        try{
            con = ConnectionManager.getConnection();
            discounts = FXCollections.observableArrayList(OrderDao.getDiscounts(con));
        } catch (Exception e) {
            e.printStackTrace();
        }
        pMethod_Bank = combopaymentmethod.getValue().split(" - ");
        //Mendapatkan ID paymentmethod yang sedang diterapkan pada comboPaymentMehtod
        for (PaymentMethod p : paymentMethods) {
            if (pMethod_Bank[0].equalsIgnoreCase(p.getPaymentMethod())&&pMethod_Bank[1].equalsIgnoreCase(p.getBank())||pMethod_Bank[0].equalsIgnoreCase(p.getPaymentMethod())&&pMethod_Bank[1].equalsIgnoreCase("null")){
                pMethodID = p.getPaymentId();
                System.out.println("mendapatkan id payment dari combobox "+pMethodID);
            }
        }
        if (discounts.isEmpty()) System.out.println("discounts is null");
        //Pengisian tabel dengan discount yang memenuhi syarat
        appliedDisc = FXCollections.observableArrayList();
        if (discounts != null) {
            System.out.println("mendapatkan id payment dari combobox #2 "+pMethodID);
            for (Discount disc : discounts) {
                System.out.println(disc.getDisc()+" "+disc.getPayment_id()+" "+disc.getRequiredProduct()+" "+disc.getRequiredType());
                boolean apply = false;
                if (disc.getPayment_id()==-1){
                    //tanpa required payment method
                    if (disc.getRequiredProduct() == null && disc.getRequiredType()==null){
                        //tanpa required product dan type
                        apply = true;
                        System.out.println("disc tanpa required apapun");
                    } else {
                        //dengan required product atau type
                        for (order o : data) {
                            if (disc.getRequiredProduct() != null && disc.getRequiredProduct().equalsIgnoreCase(o.getProductName())) {
                                apply = true;
                                System.out.println("disc tanpa required payment, dgn required product");
                                o.setTotaldisc(o.getTotalprice()*disc.getDisc());
                                o.setDicount_id(disc.getId());
                            }
                            else if (disc.getRequiredType() != null && disc.getRequiredType().equalsIgnoreCase(o.getType())) {
                                apply = true;
                                System.out.println("disc tanpa required payment, dgn required type");
                                o.setTotaldisc(o.getTotalprice()*disc.getDisc());
                                o.setDicount_id(disc.getId());
                            }
                        }

                    }//disc.getPmethod().equalsIgnoreCase(paymentMethod[0])&&disc.getBank().equalsIgnoreCase(paymentMethod[1])||disc.getPmethod().equalsIgnoreCase("cash")&&paymentMethod[0].equalsIgnoreCase("cash")
                } else if (pMethodID==disc.getPayment_id()){
                    System.out.println("payment required sudah sesuai");
                    //dengan required payment method
                    if (disc.getRequiredProduct().equalsIgnoreCase("")&&disc.getRequiredType().equalsIgnoreCase("")){
                        //tanpa required product dan type
                        apply = true;
                        System.out.println("diskon dgn required payment, tanpa required product atau type");
                    }
                    else {
                        //dengan required product dan type
                        for (order o : data) {
                            if (!disc.getRequiredProduct().equalsIgnoreCase("")&& disc.getRequiredProduct().equalsIgnoreCase(o.getProductName())) {
                                apply = true;
                                System.out.println("disc dengan required payment, dgn required product");
                                o.setTotaldisc(o.getTotalprice()*disc.getDisc());
                                o.setDicount_id(disc.getId());
                            }
                            else if (!disc.getRequiredType().equalsIgnoreCase("") && disc.getRequiredType().equalsIgnoreCase(o.getType())) {
                                apply = true;
                                System.out.println("disc dengan required payment, dgn required type");
                                o.setTotaldisc(o.getTotalprice()*disc.getDisc());
                                o.setDicount_id(disc.getId());
                            }
                        }

                    }
                }

                if (apply){
                    System.out.println("1 diskon memenuhi syarat :");
                    appliedDisc.add(disc);
                    System.out.println(disc.getDisc()+" "+disc.getPmethod()+" "+disc.getBank());
                }
            }

            System.out.println("load diskon() terjalankan");
            tablediscount.setItems(appliedDisc);
            if (!appliedDisc.isEmpty()) {
                for (int i = 0 ; i < appliedDisc.size(); i++){
                    System.out.println((i+1)+" diskon memenuhi syarat");
                }
            }

        }
    }

    @FXML
    public void loadPaymentMethod(){
        try {
            con = ConnectionManager.getConnection();
            paymentMethods = FXCollections.observableArrayList(OrderDao.reachPaymentMethod(con, "payment_id", "payment_method", "bank", "payment_method"));
            for (PaymentMethod pm:paymentMethods){
                String temp = pm.getPaymentMethod()+" - "+pm.getBank();
                combomethod.add(temp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        combopaymentmethod.setItems(combomethod);
        System.out.println("Load payment method combobox");
    }

    private Membership getMember(int member_id){
        Membership member = new Membership();
        try {
            if (!fieldcustName.getText().isEmpty()) {
                con = ConnectionManager.getConnection();
                member.setMemberName(OrderDao.getValueByIdInt(con, "member_name", "membership", "member_id", member_id, String.class));
                if (member.getMemberName() != null) {
                    member.setMemberId(member_id);
                    con = ConnectionManager.getConnection();
                    member.setPhoneNoMember(Integer.parseInt( OrderDao.getValueByIdInt(con, "member_phone", "membership", "member_id", member_id, String.class)));
                    con = ConnectionManager.getConnection();
                    member.setTotalPoints(OrderDao.getValueByIdInt(con, "total_points", "membership", "member_id", member_id, Integer.class));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return member;
    }
    @FXML
    public void cancel(ActionEvent event){
        next = false;
        totalItem = 0;
        totalPrice = 0.0;
        Stage stage = (Stage) btncancel.getScene().getWindow();
        try {
            Parent root = CashierApp.loadScene("formTabel_order.fxml");
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void clearFieldsPayment() {
        if (fieldcustName!=null) fieldcustName.clear();
        if (checkMember!=null)checkMember.setSelected(false);
        if (fieldmemberID!=null)fieldmemberID.clear();
        if (fieldpoinGain!=null)fieldpoinGain.clear();
        if (fieldcashier!=null)fieldcashier.clear();
        if (autochange!=null)autochange.clear();
        if (autototal!=null)autototal.clear();
        if (autototalQty!=null)autototalQty.clear();
        if (fieldpayment!=null)fieldpayment.clear();
        if (autototaltransaction!=null)autototaltransaction.clear();
    }
}

