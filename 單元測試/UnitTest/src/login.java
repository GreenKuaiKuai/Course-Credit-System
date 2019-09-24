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

	public void getData() throws Exception //���������ƨä��ѶǤJ�}�C
	{
		for(int i=0;i<5;i++) {
			FileReader fr = new FileReader("example"+i+".txt");
			cd.decode(fr, 106, 1);
		}
	}

	public void printChoose() //�t�Τ���
	{
		int pick = 0;
		do {
			if(pick > 2 || pick <0)
				System.out.print("\n��J���~! �Э��s��J");
			Scanner sc = new Scanner(System.in);
			System.out.print("\n�п�ܥ\�� \n"
					+ "(0)�C�X�ҵ{��T  (1)�ثe�Ǥ����p (2)���} \n");
			pick = sc.nextInt();
		}while(pick > 2 || pick <0);
		if(pick == 0) {
			printChooseClass(); //�i�J�L�X�ҵ{���e����
		}
		else if(pick == 1)
		{
			System.out.print("\n\n\n\n\n\n");
			cd.print(6); //�L�X�ثe�Ǥ����p
			printNowSituation();
		}
		else if(pick == 2)
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
					+ "(0)����  (1)�M�~��ס@  (2)���L                             (3)���ŦX�ۥѿ�� \n"
					+ "(4)�q��  (5)���Ǵ��ҵ{  (6)�^�W��\n");
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
				System.out.print("\n��J���~! �Э��s��J");
			Scanner sc = new Scanner(System.in);
			System.out.print("\n�O�_�n��ܥ��Ǵ��׽ҫᵲ�G? \n"
					+ "(0)�O  (1)�_\n");
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