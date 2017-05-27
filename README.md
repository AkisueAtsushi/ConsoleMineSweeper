# ConsoleMineSweeper
MineSweeper on CLI environment (study for Java, 6th April, 2009)

## Mine Sweeper 仕様  

最初にフィールドの大きさを設定（20×20が最大）  
セルの選択→[縦][横]を入力することで決定  
（ex. "cb" と入力の場合、縦3列目、横2列目を選択する）  
数字から動作を選択  
（1->open, 2-> add/remove flag 3-> 自分の周囲のセルをすべて開封 4->cansel  
勝敗がついた場合、もう一度ゲームをするかどうか選択可能  
１番最初にセルを開けるときは必ずボムではない  

-------------------------------------------------------------------------------  

## MineSweeperクラス関数一覧  

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

## Cellクラス 関数一覧  

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
