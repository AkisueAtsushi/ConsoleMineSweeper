/******************************************************************************
Mine Sweeper 仕様
-------------------------------------------------------------------------------
最初にフィールドの大きさを設定（20×20が最大）
セルの選択→[縦][横]を入力することで決定
（ex. "cb" と入力の場合、縦3列目、横2列目を選択する）
数字から動作を選択
（1->open, 2-> add/remove flag 3-> 自分の周囲のセルをすべて開封 4->cansel
勝敗がついた場合、もう一度ゲームをするかどうか選択可能
１番最初にセルを開けるときは必ずボムではない
-------------------------------------------------------------------------------

MineSweeperクラス関数一覧
-------------------------------------------------------------------------------
コンストラクタMineSweeper()
ゲーム全体の流れを記述

boolean makeAction(void)
セルを選んで、開封・フラグ・周りを開封の処理を選択して実行
返り値 true = 勝敗決着 false = 勝敗未決着

int countFlag(int select_w, int select_h)
引値: select_w = 選択したセルの[縦]の値 select_h = 選択したセルの[横]の値
返り値: 引値によって表されるセルの周囲のセルのフラッグの数を返す

void setBomb(int set_w, int set_h, int set_bombs)
引値: set_w = フィールドの幅 set_h = フィールドの高さ set_bombs = ボムの数
set_bombsの数だけボムを配置する

void setField(void)
フィールドの大きさをコンソールからの入力で設定

void more_open(int select_w, int select_h)
引値: select_w = 選択したセルの[縦]の値 select_h = 選択したセルの[横]の値
開封したセルが空白だった場合、その周りのセルを全て開け続ける

void showCell(void)
コンソール画面にGUIチックにマインスイーパのフィールドを表示

boolean continue_or_finish(void)
再度ゲームを新たに始めるか判定
返り値:true = ゲーム続行　false = ゲーム終了
-------------------------------------------------------------------------------

Cellクラス 関数一覧
-------------------------------------------------------------------------------
コンストラクタCell()
セルの値を初期化、セルの状態を未開封状態に

void setContent(int s)
引値: s = 値を増加させる数
そのセルの値をsだけ増やす。ただしボムの場合は加算ではなく、ボムの値を代入する

int getContent()
返り値: セルの値

void setCellState(int state)
引値: state = セルの状態を表す数字
セルの状態を設定する。

int getCellState()
返り値: セルの状態
-------------------------------------------------------------------------------

Up Date
4月26日（日）2：29　秋末
******************************************************************************/

import java.io.*;

class MineSweeper
{
	//フィールドの大きさの限界値
	final static int WIDTH_MAX = 20;
	final static int HEIGHT_MAX = 20;

	//セルの状態=クローズ
	final static byte CLOSE = 30;

	//セルの状態=フラグ
	final static byte FLAG = 20;

	//セルの状態=オープン
	final static byte OPEN = 10;

	//セルがボム
	final static byte BOMB = 11;

	//セルが空
	final static byte EMPTY = 0;

	//文字列の入力に使用
	private BufferedReader br =
													new BufferedReader(new InputStreamReader(System.in));
	private String temp;

	//フィールド
	private Cell[][] cell;

	//選択したフィールドの高さ=w, 幅=h とボムの数=bombs
	private int w=0, h=0, bombs=0;

	//コンストラクタ
	MineSweeper()
	{
		//ゲーム全体の流れ
		while(true)
		{
			setField();	//フィールドの大きさを設定

			//ゲームオーバーまで繰り返し
			while(true)
				if(makeAction()) break;

			//再度続けるか確認
			if(continue_or_finish()) break;
		}
	}

	public boolean continue_or_finish()
	{
		char yn;
		while(true)
		{
			System.out.print("Continue? y/n -> ");

			try{
				temp = br.readLine();
				yn = temp.charAt(0);
				if(yn == 'y' || yn == 'n')	break;
				else
				{
					System.out.println("Type 'y' or 'n'");
					continue;
				}
			}catch (Exception e) {
				System.out.println("Wrong.");
				continue;
			}
		}

		if(yn == 'y') return false;
		else return true;
	}

	public boolean makeAction()
	{
		int open_w, open_h, select;
		boolean is_bomb = false, is_break = false, not_opened = true;

		//すべてのセルが未開封かどうか確認
		for(int i=1; i<=w; i++)
			for(int j=1; j<=h; j++)
				if(cell[i][j].getCellState() != CLOSE) not_opened = false;

		//セルを選ぶ
		while(true){
			System.out.print("Select the cell at -> ");

			char pos1, pos2;
			try{
				temp = br.readLine();
				pos1 = temp.charAt(0);
				pos2 = temp.charAt(1);
				open_w = (int)pos1 - 96;
				open_h = (int)pos2 - 96;
				if(open_w > w || open_h > h || open_w < 0 || open_h < 0)
				{
					System.out.println("Not applyed.");
					continue;
				}
			}catch(Exception e){
				System.out.println("Wrong.");
				continue;
			}
			break;
		}

		//動作を選ぶ
		while(true){
			System.out.print("Your action:\n1->open 2->add/remove flag\n3->open all around selected cell 4->cansel: ");

			try{
				temp = br.readLine();
				select = Integer.parseInt(temp);
				if(select < 1 || select > 4)
				{
					System.out.println("Not applyed.");	continue;
				}
				else if(select == 1 && cell[open_w][open_h].getCellState() == OPEN)
				{
					System.out.println("Not applyed");	continue;
				}
				else if(select == 2 && cell[open_w][open_h].getCellState() == OPEN)
				{
					System.out.println("Not applyed");	continue;
				}
				else if(select == 3 && cell[open_w][open_h].getCellState() != OPEN)
				{
					System.out.println("Not applyed");	continue;
				}
				else if(select == 3 &&
								cell[open_w][open_h].getContent() != countFlag(open_w, open_h))
				{
					System.out.println("Not applyed");	continue;
				}
			}catch (Exception e) {
				System.out.println("Wrong.");
				continue;
			}
			break;
		}

		//動作を実行
		switch(select)
		{
			//開封
			case 1:
				cell[open_w][open_h].setCellState(OPEN);

				//すべてのセルが未開封なら、ボムを配置（1手目でボムになるのを防ぐ）
				if(not_opened) setBomb(w, h, bombs);

				if(cell[open_w][open_h].getContent() == EMPTY)
					more_open(open_w, open_h);
				else if(cell[open_w][open_h].getContent() == BOMB)	is_bomb = true;
				break;

			//フラグを立てる/消去
			case 2:
				if(cell[open_w][open_h].getCellState() == CLOSE)
					cell[open_w][open_h].setCellState(FLAG);
				else if(cell[open_w][open_h].getCellState() == FLAG)
					cell[open_w][open_h].setCellState(CLOSE);
				break;

			//選択した周りのセルをすべて開封
			case 3:
				if(cell[open_w+1][open_h].getCellState() != FLAG)
				{
					cell[open_w+1][open_h].setCellState(OPEN);

					if(cell[open_w+1][open_h].getContent() == EMPTY)
						more_open(open_w+1, open_h);
					else if(cell[open_w+1][open_h].getContent() == BOMB)	is_bomb = true;
				}

				if(cell[open_w-1][open_h].getCellState() != FLAG)
				{
					cell[open_w-1][open_h].setCellState(OPEN);

					if(cell[open_w-1][open_h].getContent() == EMPTY)
						more_open(open_w-1, open_h);
					else if(cell[open_w-1][open_h].getContent() == BOMB) is_bomb = true;
				}

				if(cell[open_w][open_h+1].getCellState() != FLAG)
				{
					cell[open_w][open_h+1].setCellState(OPEN);

					if(cell[open_w][open_h+1].getContent() == EMPTY)
						more_open(open_w, open_h+1);
					else if(cell[open_w][open_h+1].getContent() == BOMB) is_bomb = true;
				}

				if(cell[open_w][open_h-1].getCellState() != FLAG)
				{
					cell[open_w][open_h-1].setCellState(OPEN);

					if(cell[open_w][open_h-1].getContent() == EMPTY)
						more_open(open_w, open_h-1);
					else if(cell[open_w][open_h-1].getContent() == BOMB) is_bomb = true;
				}

				if(cell[open_w+1][open_h+1].getCellState() != FLAG)
				{
					cell[open_w+1][open_h+1].setCellState(OPEN);

					if(cell[open_w+1][open_h+1].getContent() == EMPTY)
						more_open(open_w+1, open_h+1);
					else if(cell[open_w+1][open_h+1].getContent() == BOMB) is_bomb = true;
				}

				if(cell[open_w+1][open_h-1].getCellState() != FLAG)
				{
					cell[open_w+1][open_h-1].setCellState(OPEN);
					if(cell[open_w+1][open_h-1].getContent() == EMPTY)
						more_open(open_w+1, open_h-1);
					else if(cell[open_w+1][open_h-1].getContent() == BOMB) is_bomb = true;
				}

				if(cell[open_w-1][open_h+1].getCellState() != FLAG)
				{
					cell[open_w-1][open_h+1].setCellState(OPEN);

					if(cell[open_w-1][open_h+1].getContent() == EMPTY)
						more_open(open_w-1, open_h+1);
					else if(cell[open_w-1][open_h+1].getContent() == BOMB) is_bomb = true;
				}

				if(cell[open_w-1][open_h-1].getCellState() != FLAG)
				{
					cell[open_w-1][open_h-1].setCellState(OPEN);

					if(cell[open_w-1][open_h-1].getContent() == EMPTY)
						more_open(open_w-1, open_h-1);
					else if(cell[open_w-1][open_h-1].getContent() == BOMB) is_bomb = true;
				}
				break;

			//キャンセル
			case 4:	break;

			default:
		}

		//勝利判定
		if(is_bomb)
		{
			System.out.println("You Lose");	is_break = true;
		}

		else
		{
			int rest = 0;
			for(int i=1; i<=w; i++)
				for(int j=1; j<=h; j++)
					if(cell[i][j].getCellState() == CLOSE ||
							cell[i][j].getCellState() == FLAG)
						rest++;

			if(rest == bombs)
			{
				for(int i=1; i<=w; i++)
					for(int j=1; j<=h; j++)
						if(cell[i][j].getCellState() == CLOSE)
							cell[i][j].setCellState(FLAG);
				System.out.println("You Win"); is_break = true;
			}
		}

		showCell();

		return is_break;
	}

	public int countFlag(int select_w, int select_h)
	{
		int count = 0;

		if(cell[select_w][select_h+1].getCellState() == FLAG)	count++;
		if(cell[select_w][select_h-1].getCellState() == FLAG)	count++;
		if(cell[select_w+1][select_h].getCellState() == FLAG)	count++;
		if(cell[select_w-1][select_h].getCellState() == FLAG)	count++;
		if(cell[select_w+1][select_h+1].getCellState() == FLAG)	count++;
		if(cell[select_w-1][select_h-1].getCellState() == FLAG)	count++;
		if(cell[select_w+1][select_h-1].getCellState() == FLAG)	count++;
		if(cell[select_w-1][select_h+1].getCellState() == FLAG)	count++;

		return count;
	}

	//ボムを配置
	public void setBomb(int set_w, int set_h, int set_bombs)
	{
		int rnd_w, rnd_h;

		for(int i=0; i<set_bombs; i++)
		{
			rnd_w = (int)(Math.round((Math.random())*100)) % set_w +1;
			rnd_h = (int)(Math.round((Math.random())*100)) % set_h +1;

			if(cell[rnd_w][rnd_h].getContent() != BOMB &&
					cell[rnd_w][rnd_h].getCellState() != OPEN)
			{
				cell[rnd_w][rnd_h].setContent(BOMB);

				//全方向の数字を増加
				if(cell[rnd_w+1][rnd_h].getContent() != BOMB)
					cell[rnd_w+1][rnd_h].setContent(1);
				if(cell[rnd_w-1][rnd_h].getContent() != BOMB)
					cell[rnd_w-1][rnd_h].setContent(1);
				if(cell[rnd_w][rnd_h+1].getContent() != BOMB)
					cell[rnd_w][rnd_h+1].setContent(1);
				if(cell[rnd_w][rnd_h-1].getContent() != BOMB)
					cell[rnd_w][rnd_h-1].setContent(1);
				if(cell[rnd_w-1][rnd_h-1].getContent() != BOMB)
					cell[rnd_w-1][rnd_h-1].setContent(1);
				if(cell[rnd_w+1][rnd_h-1].getContent() != BOMB)
					cell[rnd_w+1][rnd_h-1].setContent(1);
				if(cell[rnd_w-1][rnd_h+1].getContent() != BOMB)
					cell[rnd_w-1][rnd_h+1].setContent(1);
				if(cell[rnd_w+1][rnd_h+1].getContent() != BOMB)
					cell[rnd_w+1][rnd_h+1].setContent(1);
			}

			else
			{
				i--; continue;
			}
		}
	}

	//フィールドの設定
	public void setField()
	{
		while(true){
			System.out.print("The number of cells of Height(Max:20) -> ");

			try{
				temp = br.readLine();
				w = Integer.parseInt(temp);
				if(w > WIDTH_MAX){
						System.out.println("It's too wide. Max height is 20."); continue;
				}
				if(w <= 1)
				{
					System.out.println("Not applyed."); continue;
				}
			}catch (Exception e) {
				System.out.println("Wrong.");
				continue;
			}
			break;
		}

		while(true){
			System.out.print("The number of cells of Width(Max:20) -> ");

			try{
				temp = br.readLine();
				h = Integer.parseInt(temp);
				if(h > HEIGHT_MAX)
				{
					System.out.println("It's too wide. Max width is 20."); continue;
				}
				if(h <= 1)
				{
					System.out.println("Not applyed."); continue;
				}
			}catch (Exception e) {
				System.out.println("Wrong.");
				continue;
			}
			break;
		}

		while(true){
			System.out.print("The number of Bombs -> ");

			try{
				temp = br.readLine();
				bombs = Integer.parseInt(temp);
				if(bombs >= (w*h) || bombs <= 0)
				{
					System.out.println("Select between 1 - " + (w*h-1));
					continue;
				}
			}catch (Exception e) {
				System.out.println("Wrong.");
				continue;
			}
			break;
		}

		//フィールドの大きさに合わせてセルを作る
		cell = new Cell[w+2][h+2];

		for(int i=0; i<w+2; i++)
			for(int j=0; j<h+2; j++)
				cell[i][j] = new Cell();

		showCell();
	}

	//開けたマスが空白だったとき
	public void more_open(int select_w, int select_h)
	{
		if((select_w > 0) && (select_w <= w) && (select_h > 0) && (select_h <= h))
		{
			if(cell[select_w-1][select_h-1].getContent() == EMPTY &&
				 cell[select_w-1][select_h-1].getCellState() == CLOSE)
			{
				cell[select_w-1][select_h-1].setCellState(OPEN);
				more_open(select_w-1, select_h-1);
			}
			else cell[select_w-1][select_h-1].setCellState(OPEN);

			if(cell[select_w-1][select_h+1].getContent() == EMPTY &&
				 cell[select_w-1][select_h+1].getCellState() == CLOSE)
			{
				cell[select_w-1][select_h+1].setCellState(OPEN);
				more_open(select_w-1, select_h+1);
			}
			else cell[select_w-1][select_h+1].setCellState(OPEN);

			if(cell[select_w+1][select_h+1].getContent() == EMPTY &&
				 cell[select_w+1][select_h+1].getCellState() == CLOSE)
			{
				cell[select_w+1][select_h+1].setCellState(OPEN);
				more_open(select_w+1, select_h+1);
			}
			else cell[select_w+1][select_h+1].setCellState(OPEN);

			if(cell[select_w+1][select_h-1].getContent() == EMPTY &&
				 cell[select_w+1][select_h-1].getCellState() == CLOSE)
			{
				cell[select_w+1][select_h-1].setCellState(OPEN);
				more_open(select_w+1, select_h-1);
			}
			else cell[select_w+1][select_h-1].setCellState(OPEN);

			if(cell[select_w][select_h+1].getContent() == EMPTY &&
				 cell[select_w][select_h+1].getCellState() == CLOSE)
			{
				cell[select_w][select_h+1].setCellState(OPEN);
				more_open(select_w, select_h+1);
			}
			else cell[select_w][select_h+1].setCellState(OPEN);

			if(cell[select_w][select_h-1].getContent() == EMPTY &&
				 cell[select_w][select_h-1].getCellState() == CLOSE)
			{
				cell[select_w][select_h-1].setCellState(OPEN);
				more_open(select_w, select_h-1);
			}
			else cell[select_w][select_h-1].setCellState(OPEN);

			if(cell[select_w+1][select_h].getContent() == EMPTY &&
				 cell[select_w+1][select_h].getCellState() == CLOSE)
			{
				cell[select_w+1][select_h].setCellState(OPEN);
				more_open(select_w+1, select_h);
			}
			else cell[select_w+1][select_h].setCellState(OPEN);

			if(cell[select_w-1][select_h].getContent() == EMPTY &&
				 cell[select_w-1][select_h].getCellState() == CLOSE)
			{
				cell[select_w-1][select_h].setCellState(OPEN);
				more_open(select_w-1, select_h);
			}
			else cell[select_w-1][select_h].setCellState(OPEN);
		}
	}

	//画面表示
	public void showCell()
	{
		System.out.print("\n  ");
		for(int i=0; i<h ;i++)
		{
			System.out.print((char)('ａ'+i));
		}
		System.out.print("\n");

		for(int i=1; i<=w ; i++)
		{
			System.out.print((char)('ａ'+i+1));
			for(int j=1; j<=h; j++){
				if(cell[i][j].getCellState() == CLOSE)	System.out.print("■");
				else if(cell[i][j].getCellState() == FLAG) System.out.print("☆");
				else if(cell[i][j].getContent() == EMPTY)	System.out.print("　");
				else if(cell[i][j].getContent() != BOMB)
					System.out.print((char)(cell[i][j].getContent() + 65296));
				else System.out.print("Ｂ");
			}
			System.out.print("\n");
		}
		System.out.print("\n");
	}

	public static void main(String args[])
	{
		new MineSweeper();
	}
}
//MineSweeperクラスここまで

class Cell
{
	private int open_or_close;
	private int state;

	//コンストラクタ
	Cell(){
		state = MineSweeper.EMPTY;
		open_or_close = MineSweeper.CLOSE;
	}

	public void setContent(int s)
	{
		if(s != MineSweeper.BOMB)	state += s;
		else state = MineSweeper.BOMB;
	}

	public int getContent()
	{
		return state;
	}

	public void setCellState(int state)
	{
		open_or_close = state;
	}

	public int getCellState()
	{
		return open_or_close;
	}
}
