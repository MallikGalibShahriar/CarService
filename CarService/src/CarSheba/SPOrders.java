/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CarSheba;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author daiya
 */
public class SPOrders extends javax.swing.JFrame {

    /**
     * Creates new form SPOrders
     */
    public SPOrders() {
        initComponents();
    }
    
    public SPOrders(String ID) {
        initComponents();
        
        spId=ID;
        ArrayList<List2> listt=SPlist2();
        DefaultTableModel model=(DefaultTableModel)CarServiceTable.getModel();
        Object[] row = new Object[9];
        if(listt.size()!=0)
        {
         for(int i=listt.size()-1;i>-1;i--)
        {
            row[0]=i+1;
            row[1]=listt.get(i).getOid();
            row[2]=listt.get(i).getsName();
            row[4]=listt.get(i).getSpName();
            row[3]=listt.get(i).getsRate();
            row[5]=listt.get(i).getEmail();
            row[6]=listt.get(i).getPhone();
            row[7]=listt.get(i).getAddrs();
            row[8]=listt.get(i).getDateTime();
            model.addRow(row);
        }
        }
         
         
         
         
         
        ArrayList<List3> listt1=SPlist3();
        DefaultTableModel model1=(DefaultTableModel)CarRentTable.getModel();
        Object[] row1 = new Object[9];
        if(listt1.size()!=0)
        {
         for(int i=listt1.size()-1;i>-1;i--)
        {
            row1[0]=i+1;
            row1[1]=listt1.get(i).getOid();
            row1[2]=listt1.get(i).getCarBrand();
            row1[3]=listt1.get(i).getCarModel();
            row1[4]=listt1.get(i).getRentPrice();
            row1[5]=listt1.get(i).getSpName();
            row1[6]=listt1.get(i).getPhone();
            row1[7]=listt1.get(i).getAddrs();
            row1[8]=listt1.get(i).getDatenTime();
            model1.addRow(row1);
        }
        }
    }
    
    
    
    public ArrayList<List2> SPlist2()
     {
           ArrayList<List2> splist2=new ArrayList<>();
           
            try{
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String url="jdbc:sqlserver://localhost:1433;databaseName=PROJECT;user=sa;password=123456";
            Connection con = DriverManager.getConnection(url);
            String sql = "Select ServiceName,ServiceRate,UserTable.FirstName+' '+UserTable.LastName as spName,UserTable.Phone,PendingOrderTable.Address,UserTable.Email,PendingOrderTable.DateAndTime,PendingOrderTable.OrderId from UserTable inner join PendingOrderTable on UserTable.UserId=PendingOrderTable.UserId inner join \n" +
                            "CarService on PendingOrderTable.CarServiceId=CarService.CarServiceId inner join ServiceTable on  ServiceTable.CarServiceId=CarService.CarServiceId where ServiceProviderId=? order by OrderId";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, spId);
            ResultSet rs = pst.executeQuery();
            List2 list2;
            while(rs.next()){
                list2=new List2(rs.getString("ServiceName"),rs.getString("ServiceRate"),rs.getString("spName"),rs.getString("DateAndTime"),rs.getString("Address"),rs.getString("Email"),rs.getString("Phone"),rs.getString("OrderId"));
                splist2.add(list2);
            }
            
            
            String sql1 = "Select sum(amount) as sum,count(Orderid) as oid from UserTable inner join PendingOrderTable on UserTable.UserId=PendingOrderTable.UserId inner join \n" +
                            "CarService on PendingOrderTable.CarServiceId=CarService.CarServiceId inner join ServiceTable on  ServiceTable.CarServiceId=CarService.CarServiceId where ServiceProviderId=?";
            pst = con.prepareStatement(sql1);
            pst.setString(1, spId);
            ResultSet rs1 = pst.executeQuery();
            
            if(rs1.next())
            {
                to1.setText(rs1.getString("oid"));
                ta1.setText(rs1.getString("sum"));
            }
            
            con.close();
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
           
           
           return splist2;
     }
    
    
    
    
    
    public ArrayList<List3> SPlist3()
     {
           ArrayList<List3> splist3=new ArrayList<>();
           
            try{
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String url="jdbc:sqlserver://localhost:1433;databaseName=PROJECT;user=sa;password=123456";
            Connection con = DriverManager.getConnection(url);
            String sql = "Select CarBrand,CarModel,RentPrice,UserTable.FirstName+' '+UserTable.LastName as spName,UserTable.Phone,PendingOrderTable.Address,DateAndTime,PendingOrderTable.OrderId from UserTable\n" +
                         "inner join PendingOrderTable on UserTable.UserId=PendingOrderTable.UserId\n" +
                         "inner join CarRent on PendingOrderTable.CarRentId=CarRent.CarRentId\n" +
                         "inner join Car on Car.CarRentId=CarRent.CarRentId where ServiceProviderId=? order by OrderId";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, spId);
            ResultSet rs = pst.executeQuery();
            List3 list3;
            while(rs.next()){
                list3=new List3(rs.getString("CarBrand"),rs.getString("CarModel"),rs.getString("RentPrice"),rs.getString("spName"),rs.getString("DateAndTime"),rs.getString("Address"),rs.getString("Phone"),rs.getString("OrderId"));
                splist3.add(list3);
            }
            
            
            String sql1 = "Select count(OrderId) as oid,sum(amount) as sum from UserTable\n" +
                         "inner join PendingOrderTable on UserTable.UserId=PendingOrderTable.UserId\n" +
                         "inner join CarRent on PendingOrderTable.CarRentId=CarRent.CarRentId\n" +
                         "inner join Car on Car.CarRentId=CarRent.CarRentId where ServiceProviderId=?";
            pst = con.prepareStatement(sql1);
            pst.setString(1, spId);
            ResultSet rs1 = pst.executeQuery();
            
            if(rs1.next())
            {
                to2.setText(rs1.getString("oid"));
                ta2.setText(rs1.getString("sum"));
            }
                
            
            con.close();
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
           
           
           return splist3;
     }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        CarServiceTable = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        CarRentTable = new javax.swing.JTable();
        to1 = new javax.swing.JLabel();
        ta1 = new javax.swing.JLabel();
        to2 = new javax.swing.JLabel();
        ta2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(1262, 800));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setPreferredSize(new java.awt.Dimension(1200, 1100));

        jLabel1.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 102));
        jLabel1.setText("CAR SERVICE");

        CarServiceTable.setBackground(new java.awt.Color(245, 250, 255));
        CarServiceTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Order No.", "OrderId", "Service Name", "Amount", "Customer Name", "Customer Email", "Customer Phone", "Customer Address", "Date and time"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        CarServiceTable.setRowHeight(30);
        CarServiceTable.setRowMargin(8);
        CarServiceTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                CarServiceTableMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(CarServiceTable);
        if (CarServiceTable.getColumnModel().getColumnCount() > 0) {
            CarServiceTable.getColumnModel().getColumn(1).setMinWidth(0);
            CarServiceTable.getColumnModel().getColumn(1).setPreferredWidth(0);
            CarServiceTable.getColumnModel().getColumn(1).setMaxWidth(0);
        }

        jLabel2.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 102));
        jLabel2.setText("CAR RENT");

        CarRentTable.setBackground(new java.awt.Color(245, 250, 255));
        CarRentTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Order No.", "OrderId", "Car Brand", "Car Model", "Rent Price", "Customer Name", "Phone", "Address", "Date and Time"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        CarRentTable.setRowHeight(30);
        CarRentTable.setRowMargin(8);
        CarRentTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                CarRentTableMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(CarRentTable);
        if (CarRentTable.getColumnModel().getColumnCount() > 0) {
            CarRentTable.getColumnModel().getColumn(1).setMinWidth(0);
            CarRentTable.getColumnModel().getColumn(1).setPreferredWidth(0);
            CarRentTable.getColumnModel().getColumn(1).setMaxWidth(0);
        }

        to1.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        to1.setForeground(new java.awt.Color(0, 0, 102));
        to1.setText("jLabel3");

        ta1.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        ta1.setForeground(new java.awt.Color(0, 0, 102));
        ta1.setText("jLabel4");

        to2.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        to2.setForeground(new java.awt.Color(0, 0, 102));
        to2.setText("jLabel5");

        ta2.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        ta2.setForeground(new java.awt.Color(0, 0, 102));
        ta2.setText("jLabel6");

        jLabel3.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 102));
        jLabel3.setText("Total Pending Order   :");

        jLabel4.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 0, 102));
        jLabel4.setText("Total Amount               :");

        jLabel5.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 0, 102));
        jLabel5.setText("Total Pending Order   :");

        jLabel6.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 0, 102));
        jLabel6.setText("Total Amount               :");

        jButton4.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jButton4.setForeground(new java.awt.Color(40, 40, 40));
        jButton4.setText("PENDING ORDERS");
        jButton4.setEnabled(false);
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setBackground(new java.awt.Color(40, 40, 40));
        jButton5.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jButton5.setForeground(new java.awt.Color(240, 240, 240));
        jButton5.setText("SERVED ORDERS");
        jButton5.setToolTipText("");
        jButton5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton3.setBackground(new java.awt.Color(40, 40, 40));
        jButton3.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jButton3.setForeground(new java.awt.Color(240, 240, 240));
        jButton3.setText("ACCOUNT");
        jButton3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(40, 40, 40));
        jButton2.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jButton2.setForeground(new java.awt.Color(240, 240, 240));
        jButton2.setText("HOME");
        jButton2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(331, 331, 331))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(46, 46, 46)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 1126, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 1126, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(506, 506, 506)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(478, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(to1, javax.swing.GroupLayout.DEFAULT_SIZE, 72, Short.MAX_VALUE)
                            .addComponent(ta1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(515, 515, 515))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(to2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(ta2, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(513, 513, 513))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(57, 57, 57)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(to1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addGap(4, 4, 4)
                        .addComponent(ta1, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 354, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(to2, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ta2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addComponent(jLabel6)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, 50))
        );

        jScrollPane1.setViewportView(jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1246, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 800, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        setVisible(false);
        SPOrders2 s = new SPOrders2(spId);
        s.setVisible(true);
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        setVisible(false);
        SPAccount s = new SPAccount(spId);
        s.setVisible(true);

    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        setVisible(false);
        ServiceProviderHome s = new ServiceProviderHome(spId);
        s.setVisible(true);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void CarServiceTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CarServiceTableMouseClicked
        // TODO add your handling code here:


    int index =CarServiceTable.getSelectedRow();
    TableModel model=CarServiceTable.getModel();
    String id=model.getValueAt(index,1).toString();
    
        
    int dialogButton = JOptionPane.YES_NO_OPTION;
    int dialogResult = JOptionPane.showConfirmDialog(this, "Are sure you want to mark this as served?", "Title on Box", dialogButton);
    if(dialogResult == 0) {
     // System.out.println("Yes option");
     try{
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar calobj = Calendar.getInstance();
            String date;
            date=(df.format(calobj.getTime())).toString();
            
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String url="jdbc:sqlserver://localhost:1433;databaseName=PROJECT;user=sa;password=123456";
            Connection con = DriverManager.getConnection(url);
            
            String sql="select * from PendingOrderTable where OrderId=?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, id); 
            ResultSet rs = pst.executeQuery();
            
            if(rs.next()){
                carServiceId=(rs.getString("CarServiceId"));
                uid=(rs.getString("UserId"));
                serviceRate=(rs.getString("amount"));
                add=(rs.getString("Address"));
                
            }
            
           // String carServiceId,uid,serviceRate,date;
            sql = "Insert into OrderTable"
            +"(CarRentId,CarServiceId,UserId,amount,DateAndTime,Address)"
            +"values(?,?,?,?,?,?)";
            pst = con.prepareStatement(sql);
            pst.setString(2,carServiceId);
            pst.setInt(3, Integer.parseInt(uid));
            pst.setString(4,serviceRate);
            pst.setInt(1, 0);
            pst.setString(5,date);
            pst.setString(6,add);

            pst.executeUpdate();
            

            sql = "delete from [PROJECT].[dbo].[PendingOrderTable] where OrderId=?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(id));
            pst.executeQuery();

            con.close();
            
        }
        catch(Exception e){
           // JOptionPane.showMessageDialog(null, e);
           setVisible(false);
            SPOrders s = new SPOrders(spId);
            s.setVisible(true);
        }
     
    } else {
      System.out.println("No Option");
    }

       // setVisible(false);
       // CarRentDetails s = new CarRentDetails(id,uid);
       // s.setVisible(true);
    }//GEN-LAST:event_CarServiceTableMouseClicked

    private void CarRentTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CarRentTableMouseClicked
        // TODO add your handling code here:
        
            int index =CarRentTable.getSelectedRow();
            TableModel model=CarRentTable.getModel();
            String id=model.getValueAt(index,1).toString();
            
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar calobj = Calendar.getInstance();
            String date;
            date=(df.format(calobj.getTime())).toString();


            int dialogButton = JOptionPane.YES_NO_OPTION;
            int dialogResult = JOptionPane.showConfirmDialog(this, "Are sure you want to mark this as served?", "Title on Box", dialogButton);
            if(dialogResult == 0) {
             // System.out.println("Yes option");
             try{


                    Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                    String url="jdbc:sqlserver://localhost:1433;databaseName=PROJECT;user=sa;password=123456";
                    Connection con = DriverManager.getConnection(url);

                    String sql="select * from PendingOrderTable where OrderId=?";
                    PreparedStatement pst = con.prepareStatement(sql);
                    pst.setString(1, id); 
                    ResultSet rs = pst.executeQuery();

                    if(rs.next()){
                        carRentId=(rs.getString("CarRentId"));
                        uid=(rs.getString("UserId"));
                        serviceRate=(rs.getString("amount"));
                        add=(rs.getString("Address"));

                    }

                   // String carServiceId,uid,serviceRate,date;
                    sql = "Insert into OrderTable"
                    +"(CarRentId,CarServiceId,UserId,amount,DateAndTime,Address)"
                    +"values(?,?,?,?,?,?)";
                    pst = con.prepareStatement(sql);
                    pst.setInt(2,0);
                    pst.setInt(3, Integer.parseInt(uid));
                    pst.setString(4,serviceRate);
                    pst.setString(1, carRentId);
                    pst.setString(5,date);
                    pst.setString(6,add);

                    pst.executeUpdate();


                    sql = "delete from [PROJECT].[dbo].[PendingOrderTable] where OrderId=?";
                    pst = con.prepareStatement(sql);
                    pst.setInt(1, Integer.parseInt(id));
                    pst.executeQuery();

                    con.close();

                }
                catch(Exception e){
                   // JOptionPane.showMessageDialog(null, e);
                   setVisible(false);
                    SPOrders s = new SPOrders(spId);
                    s.setVisible(true);
                }

            } else {
              System.out.println("No Option");
            }
    }//GEN-LAST:event_CarRentTableMouseClicked

    /**
     * @param args the command line arguments
     */
    String spId;
    String carServiceId,carRentId,uid,serviceRate,date,add;
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(SPOrders.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SPOrders.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SPOrders.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SPOrders.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SPOrders().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable CarRentTable;
    private javax.swing.JTable CarServiceTable;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel ta1;
    private javax.swing.JLabel ta2;
    private javax.swing.JLabel to1;
    private javax.swing.JLabel to2;
    // End of variables declaration//GEN-END:variables
}
