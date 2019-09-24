package test1;

public class ClassInfo
{
	private int year;
	private int term;
	private String serial_num; // 課程代號
	private String class_name; // 課程名稱
	private short necessary; // 必修 = 1 ,選修 = 0
	private float point; // 學分
	private float score; // 分數, 未取得分數 = -1

	public ClassInfo(int y, int t, String sn, String cn, short n, float p, float s)
	{
		year = y;
		term = t;
		serial_num = sn;
		class_name = cn;
		necessary = n;
		point = p;
		score = s;
	}

	public int getYear()
	{
		return year;
	}

	public int getTerm()
	{
		return term;
	}

	public String getSerialNum()
	{
		return serial_num;
	}

	public String getClassName()
	{
		return class_name;
	}

	public short getNecessary()
	{
		return necessary;
	}

	public float getPoint()
	{
		return point;
	}

	public float getScore()
	{
		return score;
	}
}
