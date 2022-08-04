package helper;

import java.util.ArrayList;

import object.FamilyMember;

public class CartHelper {

    // the purpose of cart is just a booking order list
    // containing several name and date entered

    String date_ordered;
    ArrayList<FamilyMember> anggota ;
    boolean bersamaMereka;
    String status;
    // status can be : COMPLETED or PENDING
    private int total;
    // total is not decimal value


    public CartHelper(String dataOrderan){

        // reading from the string of local data
        // using csv format
        String dataMentah [] = dataOrderan.split()

    }

    // get the member that hasnt scheduled yet
    public String getIncompletedMember(){
        String nama= null;
        for(FamilyMember mb: anggota){
                if(mb.getDate_ordered() == null){
                    nama = mb.getNama();
                    break;
                }
        }

        return nama;
    }

    public boolean isCompleted(){
        boolean semuaSelesai = false;
        int jumlahOrder = 0;

        for(FamilyMember mb: anggota){
            if(mb.getDate_ordered() == null){
                semuaSelesai = false;
                break;
            }else{
                jumlahOrder++;
            }
        }

        if(jumlahOrder == anggota.size()){
            semuaSelesai = true;
        }

        return semuaSelesai;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
