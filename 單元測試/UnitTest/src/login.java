import java.io.FileReader;
import java.util.Scanner;

public class login {

	static ClassDivider cd;

	public static void main(String[] args) throws Exception
	{
		cd = new ClassDivider();
		login demo = new login();
		demo.getData();
		demo.printChoose();
	}

	public void getData() throws Exception //抓取網頁資料並分解傳入陣列
	{
		for(int i=0;i<5;i++) {
			FileReader fr = new FileReader("example"+i+".txt");
			cd.decode(fr, 106, 1);
		}
	}

	public void printChoose() //系統介面
	{
		int pick = 0;
		do {
			if(pick > 2 || pick <0)
				System.out.print("\n輸入錯誤! 請重新輸入");
			Scanner sc = new Scanner(System.in);
			System.out.print("\n請選擇功能 \n"
					+ "(0)列出課程資訊  (1)目前學分狀況 (2)離開 \n");
			pick = sc.nextInt();
		}while(pick > 2 || pick <0);
		if(pick == 0) {
			printChooseClass(); //進入印出課程內容介面
		}
		else if(pick == 1)
		{
			System.out.print("\n\n\n\n\n\n");
			cd.print(6); //印出目前學分狀況
			printNowSituation();
		}
		else if(pick == 2)
		{
			System.exit(0);
		}
		
	}
	public void printChooseClass() //印出課程內容介面
	{
		int pc = 0;
		do {
			if(pc > 7 || pc <0)
				System.out.print("\n輸入錯誤! 請重新輸入");
			Scanner sc = new Scanner(System.in);
			System.out.print("\n請選擇欲印出的課程 \n"
					+ "(0)必修  (1)專業選修　  (2)未過                             (3)不符合自由選修 \n"
					+ "(4)通識  (5)本學期課程  (6)回上頁\n");
			pc = sc.nextInt();
		}while(pc > 6 || pc <0);
		if(pc >= 0 && pc <= 5) {
			System.out.print("\n\n\n\n\n\n");
			cd.print(pc);
			printChooseClass();
		}
		else {
			printChoose();
		}
	}
	
	public void printNowSituation() {
		int pc = 0;
		do {
			if(pc > 1 || pc <0)
				System.out.print("\n輸入錯誤! 請重新輸入");
			Scanner sc = new Scanner(System.in);
			System.out.print("\n是否要顯示本學期修課後結果? \n"
					+ "(0)是  (1)否\n");
			pc = sc.nextInt();
		}while(pc > 1 || pc <0);
		if(pc == 0) {
			System.out.print("\n\n\n\n\n\n");
			cd.print(7);
			printChoose();
		}
		else {
			printChoose();
		}
	}
}