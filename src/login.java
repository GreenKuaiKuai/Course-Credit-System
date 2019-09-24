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
		java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF); // ����ĵ�ilog
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
		// �ϥΪ̿�J�b�K
		Scanner sc = new Scanner(System.in);
		System.out.println("�w��ϥξǤ��p��t��! �п�J�հȦ�F�t�αb�K");
		System.out.print("�b��: ");
		acc = sc.nextLine();
		char passwordArray[] = console.readPassword("�K�X(�N�����): ");
		pwd=new String(passwordArray);

		
		// �}�l�n�J�{��
		System.out.println("-------------�n�J��-------------");
		webClient.getOptions().setCssEnabled(false);// �T��CSS
		page = (HtmlPage) webClient.getPage("http://ip67154.ntcu.edu.tw/ntctc/in.html");
		form = page.getFormByName("f1"); // form��name�Of1
		form.getInputByName("Account").setValueAttribute(acc); // �b��
		HtmlInput passWordInput = form.getInputByName("Password"); // �K�X
		passWordInput.removeAttribute("disabled");
		passWordInput.setValueAttribute(pwd);
		page = form.getInputByValue("�n�J").click(); // ���U�n�J���᪺����
		if(page.asText().substring(0,10).equals("�հȦ�F�t�εn�J����"))//�b���K�X���~�����p
		{
			System.out.println("�b���αK�X���~! �Э��s�n�J");
			submit();
		}
		else
		{
		System.out.println("------------�n�J���\------------\n");
		}
	}
	
	public void getTonsi() throws Exception //����q�ѾǤ�
	{
		page = (HtmlPage) webClient.getPage("http://ip67154.ntcu.edu.tw/ntctc/Grd/Grd03.asp");// ���즨�Z����
		cd.GEDecode(page.asText());
		//System.out.print(page.asText());
	}

	public String pageText(String year, String term) throws Exception//�̾Ǧ~�M�Ǵ��������
	{
		HtmlForm form2 = page.getFormByName("form1");
		HtmlSelect state = form2.getSelectByName("cboYear");// �Ǧ~
		page = state.setSelectedAttribute(year, true);

		form2 = page.getFormByName("form1");
		HtmlSelect state2 = form2.getSelectByName("cboTerm");// �Ǵ�
		page = state2.setSelectedAttribute(term, true);
		
		//�^�Ǻ�����r
		String a = page.asText();
		return a;
	}

	public void getData() throws Exception //���������ƨä��ѶǤJ�}�C
	{
		page = (HtmlPage) webClient.getPage("http://ip67154.ntcu.edu.tw/ntctc/Grd/Grd01.asp");// ���즨�Z����
		System.out.println("---------------------------------");
		System.out.println("-----��ƳB�z���Э@�ߵ���....-----");
		System.out.println("---------------------------------");
		
		int firstyear = Integer.valueOf(acc.substring(3, 6));
		for (int i = firstyear; i <= 108; i++)
			for (int j = 1; j <= 2; j++)
			{
				String info = pageText(Integer.toString(i),Integer.toString(j));
				cd.decode(info,i,j);
			}
	}

	public void printChoose() //�t�Τ���
	{
		int pick = 0;
		do {
			if(pick >3 || pick <0)
				System.out.print("\n��J���~! �Э��s��J");
			Scanner sc = new Scanner(System.in);
			System.out.print("\n�п�ܥ\�� \n"
					+ "(0)�C�X�ҵ{��T  (1)�ثe�Ǥ����p (2)�s�@�H���W�� (3)���} \n");
			pick = sc.nextInt();
		}while(pick > 3 || pick <0);
		if(pick == 0) {
			printChooseClass(); //�i�J�L�X�ҵ{���e����
		}
		else if(pick == 1)
		{
			System.out.print("\n\n\n\n\n\n");
			cd.print(7); //�L�X�ثe�Ǥ����p
			printNowSituation();
		}
		else if(pick==2)
		{
			System.out.print("\n�����N�B�d��ޱ�B���a���B���r�ʡB�G����\n" + 
					"�����¡B������BĬ���ۡB���ç�\n");
			printChoose();
		}
		else if(pick == 3)
		{
			System.exit(0);
		}
		
	}
	public void printChooseClass() //�L�X�ҵ{���e����
	{
		int pc = 0;
		do {
			if(pc > 7 || pc <0)
				System.out.print("\n��J���~! �Э��s��J");
			Scanner sc = new Scanner(System.in);
			System.out.print("\n�п�ܱ��L�X���ҵ{ \n"
					+ "(0)����  (1)�M�~��ס@  (2)���L    (3)���ŦX�ۥѿ�� \n"
					+ "(4)�q��  (5)���Ǵ��ҵ{  (6)�q��(�֤߫D�֤ߦC��) (7)�^�W��\n");
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
				System.out.print("\n��J���~! �Э��s��J");
			Scanner sc = new Scanner(System.in);
			System.out.print("\n�O�_�n��ܥ��Ǵ��׽ҫᵲ�G? \n"
					+ "(0)�O  (1)�_\n");
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