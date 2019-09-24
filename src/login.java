import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.*;
import java.util.Scanner;
import java.util.logging.Level;
import java.io.Console;

public class login {

	WebClient webClient = new WebClient(BrowserVersion.CHROME);
	HtmlPage page;
	HtmlForm form;
	String acc = "";
	String pwd = "";
	static ClassDivider cd;

	public static void main(String[] args) throws Exception
	{
		java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF); // 關閉警告log
		java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF);
		cd = new ClassDivider();
		login demo = new login();
		demo.submit();
		demo.getData();
		demo.getTonsi();
		demo.printChoose();
	}

	public void submit() throws Exception
	{
		Console console = System.console();
		// 使用者輸入帳密
		Scanner sc = new Scanner(System.in);
		System.out.println("歡迎使用學分計算系統! 請輸入校務行政系統帳密");
		System.out.print("帳號: ");
		acc = sc.nextLine();
		char passwordArray[] = console.readPassword("密碼(將不顯示): ");
		pwd=new String(passwordArray);

		
		// 開始登入程序
		System.out.println("-------------登入中-------------");
		webClient.getOptions().setCssEnabled(false);// 禁用CSS
		page = (HtmlPage) webClient.getPage("http://ip67154.ntcu.edu.tw/ntctc/in.html");
		form = page.getFormByName("f1"); // form的name是f1
		form.getInputByName("Account").setValueAttribute(acc); // 帳號
		HtmlInput passWordInput = form.getInputByName("Password"); // 密碼
		passWordInput.removeAttribute("disabled");
		passWordInput.setValueAttribute(pwd);
		page = form.getInputByValue("登入").click(); // 按下登入之後的網頁
		if(page.asText().substring(0,10).equals("校務行政系統登入首頁"))//帳號密碼錯誤的情況
		{
			System.out.println("帳號或密碼錯誤! 請重新登入");
			submit();
		}
		else
		{
		System.out.println("------------登入成功------------\n");
		}
	}
	
	public void getTonsi() throws Exception //抓取通識學分
	{
		page = (HtmlPage) webClient.getPage("http://ip67154.ntcu.edu.tw/ntctc/Grd/Grd03.asp");// 跳到成績網頁
		cd.GEDecode(page.asText());
		//System.out.print(page.asText());
	}

	public String pageText(String year, String term) throws Exception//依學年和學期抓取網頁
	{
		HtmlForm form2 = page.getFormByName("form1");
		HtmlSelect state = form2.getSelectByName("cboYear");// 學年
		page = state.setSelectedAttribute(year, true);

		form2 = page.getFormByName("form1");
		HtmlSelect state2 = form2.getSelectByName("cboTerm");// 學期
		page = state2.setSelectedAttribute(term, true);
		
		//回傳網頁文字
		String a = page.asText();
		return a;
	}

	public void getData() throws Exception //抓取網頁資料並分解傳入陣列
	{
		page = (HtmlPage) webClient.getPage("http://ip67154.ntcu.edu.tw/ntctc/Grd/Grd01.asp");// 跳到成績網頁
		System.out.println("---------------------------------");
		System.out.println("-----資料處理中請耐心等候....-----");
		System.out.println("---------------------------------");
		
		int firstyear = Integer.valueOf(acc.substring(3, 6));
		for (int i = firstyear; i <= 108; i++)
			for (int j = 1; j <= 2; j++)
			{
				String info = pageText(Integer.toString(i),Integer.toString(j));
				cd.decode(info,i,j);
			}
	}

	public void printChoose() //系統介面
	{
		int pick = 0;
		do {
			if(pick >3 || pick <0)
				System.out.print("\n輸入錯誤! 請重新輸入");
			Scanner sc = new Scanner(System.in);
			System.out.print("\n請選擇功能 \n"
					+ "(0)列出課程資訊  (1)目前學分狀況 (2)製作人員名單 (3)離開 \n");
			pick = sc.nextInt();
		}while(pick > 3 || pick <0);
		if(pick == 0) {
			printChooseClass(); //進入印出課程內容介面
		}
		else if(pick == 1)
		{
			System.out.print("\n\n\n\n\n\n");
			cd.print(7); //印出目前學分狀況
			printNowSituation();
		}
		else if(pick==2)
		{
			System.out.print("\n陳紫淇、吳婉瑄、王家偉、王臆銘、鄭羽倢\n" + 
					"童智威、黃郁喬、蘇奕倫、陳秉宏\n");
			printChoose();
		}
		else if(pick == 3)
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
					+ "(0)必修  (1)專業選修　  (2)未過    (3)不符合自由選修 \n"
					+ "(4)通識  (5)本學期課程  (6)通識(核心非核心列表) (7)回上頁\n");
			pc = sc.nextInt();
		}while(pc > 7 || pc <0);
		if(pc >= 0 && pc <= 6) {
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
			cd.print(8);
			printChoose();
		}
		else {
			printChoose();
		}
	}
}