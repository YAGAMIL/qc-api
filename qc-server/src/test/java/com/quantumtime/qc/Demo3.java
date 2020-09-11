
public class Demo3 {

    public static void main(String[] args) {

        int m=0;   //假设会传给程序一个  m 代表笼子里面脚的总数，在此先默认为 0
        if(m<=0) {
            System.out.println("笼子里没有动物");
        }else if(m==2) {
            System.out.println("笼子只有一只鸭子");
        }else if(m==4) {
            //最多
            System.out.println("笼子里最多有"+String.valueOf(m/2)+"只动物");

            //最少
            Integer minAnswer = minTest(m);
            System.out.println("笼子里最少有"+String.valueOf(minAnswer)+"只动物");


        }
    }
    //最少有多少只动物
    public static Integer minTest(int m) {
        int i = 0;
        //1：处于4      结果=i
        //2：看  i 是不是4的倍数，如果是则i为最终结果       否则i+1为最终结果
        return i;
    }


}
