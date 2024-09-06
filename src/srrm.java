import java.util.*;
class a{
    String name="sriram";
}
class b extends a{
    
    String age;
    public void display(String name,String age){
        super(name);
        System.out.println(name +""+ age);
    }
}
class srm{
    public static void main(String[] args){

        b obj=new b();
        b.display(19);
    }
}