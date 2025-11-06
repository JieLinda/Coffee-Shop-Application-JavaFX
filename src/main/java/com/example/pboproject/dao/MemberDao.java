package com.example.pboproject.dao;

import java.sql.*;
import java.util.ArrayList;
import com.example.pboproject.beans.*;
import com.example.pboproject.utils.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class MemberDao {
    public static Membership get(int id) throws SQLException {
        //mengeluarkan member yang idnya terpilih
        ResultSet rs;
        Membership member = new Membership();
        Connection con = ConnectionManager.getConnection();
        String query = String.format("select * from membership where id = s%;",id);
        try {
            PreparedStatement ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            member.setMemberId(rs.getInt("member_id"));
            member.setMemberName(rs.getString("member_name").trim());
            member.setPhoneNoMember(rs.getInt("member_phone"));
            con.close();
            rs.close();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return member;
    }
    public static ArrayList<Membership> getAll(Connection con){
        //mengeluarkan smeua member
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = "select * from membership where status = 1 order by member_id;";
        ArrayList<Membership> listmember = new ArrayList<>();
        try {
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            while(rs.next()) {
                Membership member = new Membership();
                member.setMemberId(rs.getInt("member_id"));
                member.setMemberName(rs.getString("member_name"));
                member.setPhoneNoMember(rs.getInt("member_phone"));
                listmember.add(member);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            ConnectionManager.close(ps, rs);
        }
        return listmember;
    }
    public static void save(Membership Membership) throws SQLException {
        //input member baru dan fungsi button save
        try {
            Connection con = ConnectionManager.getConnection();
            String query = "Insert into membership (member_name, member_phone, total_points) values (?,?,0::integer);";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1,Membership.getMemberName());
            ps.setInt(2,Membership.getPhoneNoMember());
            ps.executeUpdate();
            con.close();
            ps.close();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }
    public static void update(Membership member) throws SQLException {
        //fungsi button edit
        Connection con = ConnectionManager.getConnection();
        String query = "update membership set member_name = ?, member_phone = ? where member_id = ?;";
        try {
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1,member.getMemberName());
            ps.setInt(2,member.getPhoneNoMember());
            ps.setInt(3,member.getMemberId());
            ps.executeUpdate();
            con.close();
            ps.close();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    public static void delete(int id) throws SQLException {
        //delete menggunakan member id
        Connection con = ConnectionManager.getConnection();
        String query = "update membership set status=0 where member_id=?;";
        try {
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1,id);
            ps.executeUpdate();
            con.close();
            ps.close();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public static ObservableList<Membership> reachMember (Connection con, String id, String name, String phone, String totalpoint) throws SQLException {
        ObservableList<Membership> dataColumn = FXCollections.observableArrayList();
        Statement st = null;
        ResultSet rs = null;
        String columns = id+","+name+","+phone+","+totalpoint;
        String q = "select distinct "+columns+" from membership where status = 1";
        try {
            st = con.createStatement();
            rs = st.executeQuery(q);
            while (rs.next()) {
                Membership m = new Membership();
                m.setMemberId(rs.getInt(id));
                m.setMemberName(rs.getString(name));
                m.setPhoneNoMember(rs.getInt(phone));
                m.setTotalPoints(rs.getInt(totalpoint));
                dataColumn.add(m);
            }
        } catch (SQLException e){
            throw new RuntimeException(e);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
            ConnectionManager.closeConnection(con);
        }
        return dataColumn;
    }

}

