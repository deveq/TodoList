# Todo List
#### 개인프로젝트, Kotlin, Android Studio

## 애플리케이션 설명
간단히 할 일을 추가할 수 있는 Todo List 애플리케이션입니다.

## 순서
- 0. 완성 화면
- 1. 사용한 개념 및 기능
- 2. 느낀점

### 완성화면
#### 할 일 추가
![add_todo](https://user-images.githubusercontent.com/66777885/97325733-a2d64d00-18b6-11eb-97e4-dff04270d94b.gif)
<pre><code>MyAdapter.kt
    // 메뉴 추가
    todo_add.setOnClickListener {
        if (todo_input.text.toString() != "") {
           val todo = Todo(todo_input.text.toString())
            viewModel.insert(todo)
            setList()
            todo_input.setText("")
        }
    }</code></pre>
하단의 EditText에 내용을 입력 후 '추가' Button을 누르면 viewModel객체 내의 MutableLiveData의 value의 값이 변경되도록 설정함.

#### 상세보기 (상세설정)
![detail](https://user-images.githubusercontent.com/66777885/97325823-b8e40d80-18b6-11eb-8102-ebd4c3b6c95d.gif)
<pre><code>MainActivity.kt
fun goToDetail(todo: Todo, position: Int) {
        val intent = Intent(this, DetailActivity::class.java)
        val bundle = Bundle()
        bundle.putSerializable("todo", todo)
        intent.putExtra("data", bundle)
        intent.putExtra("position", position)
        startActivityForResult(intent, RC_GO_TO_DETAIL)
    }</code></pre>
    
RecyclerView의 item이 클릭되면 Adapter로 전달된 goToDeatil메서드가 실행됨.
bundle에 Serializable을 구현한 Todo객체를 넣어주고, 
DetailActivity에서 Todo객체를 받아와 처리하는 방식으로 진행
    
#### 완료하기
![done](https://user-images.githubusercontent.com/66777885/97325869-c4373900-18b6-11eb-9b2f-de512f59cc03.gif)
<pre><code>MyAdapter.kt
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // position에 해당하는 Todo객체를 얻음
        val todo = itemList[position]

        holder.todoText.text = todo.text
        
        // todo객체의 isDone을 CheckBox의 isChecked에 set해줌
        holder.todoIsDone.isChecked = todo.isDone
        
        // todo가 완료(done)된 상태라면 todo_text의 글자색 변경 후 취소선을 추가
        if (todo.isDone) {
            holder.todoText.apply {
                setTextColor(Color.GRAY)
                paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                setTypeface(null, Typeface.ITALIC)
            }
        } else {
            // 완료상태가 아니라면 글자색 복구, 취소선 없앰
            holder.todoText.apply {
                setTextColor(Color.BLACK)
                paintFlags = 0
                setTypeface(null, Typeface.NORMAL)
            }
        }
        
        // CheckBox인 todoIsDone이 클릭되었을 때
        holder.todoIsDone.apply {
            setOnClickListener {
                todo.isDone = this.isChecked
                viewModel.update(todo)
                //변경이 완료되었으므로 다시 todoList를 받아온 후 
                // liveData.value에 넣어주는 setList메서드 실행
                setList()
            }
        }
        ...
        ..
        .
        </code></pre>

#### 삭제하기
![delete](https://user-images.githubusercontent.com/66777885/97325903-cc8f7400-18b6-11eb-9936-d98a1f8cdded.gif)

<pre><code>'X'를 눌러 할 일(아이템) 한 개 삭제하기
MyAdapter.kt
override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
    ...
    ..
    .
    //아이템 내의 x버튼을 누를 경우 삭제여부 확인.
    holder.todoDelete.setOnClickListener {
      val alertDialog = AlertDialog.Builder(context)
        .setMessage("정말 삭제하시겠습니까?")
        .setPositiveButton("삭제") {str, dialogInterface ->
          val todo = itemList[position]
          viewModel.delete(todo)
          setList()
        }
      .setNegativeButton("취소",null)
    
    alertDialog.show()
    }
}
</code></pre>

<pre><code>상단 메뉴의 완료 일괄삭제
MainActivity.kt
override fun onOptionsItemSelected(item: MenuItem): Boolean {
  when(item.itemId) {
    ...
    ..
    .
    R.id.menu_delete_done -> {
      val alertDialog = AlertDialog.Builder(this)
      alertDialog.setMessage("완료된 할 일 목록을 전체 지우시겠습니까?")
        .setNegativeButton("취소", null)
        .setPositiveButton("확인") { _, _ ->
          for (todo in todayAdapter.itemList) {
            if (todo.isDone) {
              viewModel.delete(todo)
            }
          }
          setList()
        }
        .show()
    }
 </code></pre>

#### 검색하기
![search](https://user-images.githubusercontent.com/66777885/97325931-d44f1880-18b6-11eb-8fd8-8d9f2b695433.gif)
<pre><code>
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)

        val menuItem = menu?.findItem(R.id.menu_search)
        val searchView = menuItem?.actionView  as SearchView

        //검색 기능
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                //검색어 입력버튼을 누른 후 내용이 있다면
                if (query != "" && query != null) {
                    //검색어를 통해 TodoList를 얻어옴.
                    todoList.value = viewModel.getTodosByText(query)
                } else {
                    setList()
                }
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        //검색 닫기를 누른 후 기본 TodoList로 복귀
       searchView.setOnCloseListener {
            setList()
            false
        }
        return super.onCreateOptionsMenu(menu)
    }
</code></pre>

<pre><code>
TodoViewModel.kt
    val mutableLiveData = MutableLiveData<MutableList<Todo>>(todos)

    //할일 키워드로 얻기
    fun getTodosByText(text: String) : MutableList<Todo> {
        return todoDao.getTodosByText(text)
    }
</code></pre>
<pre><code>
TodoDao.kt
    //할일 키워드로 얻기
    @Query("select * from todo where text like '%' ||:text || '%' order by date,time desc")
    fun getTodosByText(text: String) : MutableList<Todo>
    </code></pre>


#### 정렬하기
![sort](https://user-images.githubusercontent.com/66777885/97325967-dadd9000-18b6-11eb-832b-a2060169c45d.gif)
<pre><code>검색하기
MainActivity.kt
//메뉴 이벤트 처리
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            //등록일 기준 정렬
            R.id.menu_sort_register -> {
                viewModel.isTimeOrder = false

            }
            //날짜 기준 정렬
            R.id.menu_sort_date -> {
                viewModel.isTimeOrder = true
            }
            ...
            ..
            .
</code></pre>

<pre><code>TodoDao.kt
    @Query("select * from Todo order by registerTime desc")
    fun getAll() : MutableList<Todo>

    //전체 얻기. 날짜순으로 얻기.
    @Query("select * from todo order by date, time desc")
    fun getAllTimeOrder() : MutableList<Todo>
</code></pre>



### Room, LiveData, ViewModel
Room, LiveData, ViewModel을 이용한 MVVM패턴을 활용하여
MutableLiveData<MutableList<Todo>>타입으로 받고, Todo객체를 item_list의 View에 바인딩 해줌으로써
화면에 목록으로써 구현됨.
 *Room
  - Entity 생성
  <pre><code>@Entity
data class Todo(var text: String?, var isDone: Boolean = false) : Serializable {
    // 등록하는 순간 System의 시간을 받으므로 등록일 순 정렬에 사용할 기준이 됨.
    @PrimaryKey
    var registerTime : Long = System.currentTimeMillis()

    @ColumnInfo
    var hashTag: String? = null

    // 날짜 기준 검색의 기준이 됨.
    @ColumnInfo
    var time: String? = null
    @ColumnInfo
    var date: String? = null
    @ColumnInfo
    var dateLong: Long? = null
    ...
    ..
    .
</code></pre>
  - Dao 생성
 <pre><code>
 @Dao
interface TodoDao {
    //전체 얻기. 기본값. 등록순서로 보이기
    @Query("select * from Todo order by registerTime desc")
    fun getAll() : MutableList<Todo>

    //전체 얻기. 날짜순으로 얻기.
    @Query("select * from todo order by date, time desc")
    fun getAllTimeOrder() : MutableList<Todo>

    //할일 키워드로 얻기
    @Query("select * from todo where text like '%' ||:text || '%' order by date,time desc")
    fun getTodosByText(text: String) : MutableList<Todo>
    ....
    ...
    ..
    .
</code></pre>

 - Database 생성
 <pre><code>
 @Database(entities = [Todo::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao
}
</code></pre>
  
 2. ViewModel
 <pre><code>
 // TodoViewModel 객체를 생성할 때 Application을 생성자에 넣어줘야하기때문에,
// 생성자가 있는 ViewModel을 생성하기 위해서는 내가 만들고자 하는 viewModel을 AndroidViewModel(application)을 상속받아 생성하
// ViewModel을 상속받은 경우는 ViewModelProvider.Factory 인터페이스를 구현한 클래스를 이용해서 생성해야함.

// https://readystory.tistory.com/176 : [준비된 개발자]님의 [Android] AAC ViewModel 을 생성하는 6가지 방법 - ViewModelProvider 참조했습니다.

class ViewModelProviderFactory(val context: Context) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(TodoViewModel::class.java)) {
            TodoViewModel(context) as T
        } else {
            throw IllegalArgumentException()
        }
    }
}</code></pre>
 
 <pre><code>
 class TodoViewModel(context: Context) : ViewModel() {
    //RoomDatabase 객체를 받아옴.
    private val todoDatabase = Room.databaseBuilder(context, AppDatabase::class.java, "todo")
        .allowMainThreadQueries()
        .fallbackToDestructiveMigration()
        .build()

    private val todoDao = todoDatabase.todoDao()
    private val todos = todoDao.getAll()
    val mutableLiveData = MutableLiveData<MutableList<Todo>>(todos)

    // 정렬 방식 선택 후(등록일 or 날짜) 그 정렬 방식으로 계속 정렬되게끔 하는 flag
    // false : 등록일 기준 정렬, true : 날짜 기준 정렬
    var isTimeOrder: Boolean = false

    fun getList(isTimeOrder: Boolean): MutableList<Todo> {
        return if (isTimeOrder) getAllTimeOrder() else getAll()
    }

    fun getAll() : MutableList<Todo> {
        return todoDao.getAll()
    }

    //전체 얻기. 날짜순으로 얻기.
    fun getAllTimeOrder() : MutableList<Todo> {
        return todoDao.getAllTimeOrder()
    }

    //할일 키워드로 얻기
    fun getTodosByText(text: String) : MutableList<Todo> {
        return todoDao.getTodosByText(text)
    }
    ....
    ...
    ..
    .
</code></pre>

3. LiveData
TodoDao객체에서 todoList 검색 결과를 받은 후 MutableLiveData<List<Todo>>객체 생성 시 인자로 해당 todoList를 넣어주어 생성
  <pre><code>val mutableLiveData = MutableLiveData<MutableList<Todo>>(todos)
</code></pre>
 <pre><code>    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //뷰모델 받아오기
        viewModel = ViewModelProvider(this, ViewModelProviderFactory(this.application))
            .get(TodoViewModel::class.java)

        //recycler view에 보여질 아이템 Room에서 받아오기
        todoList = viewModel.mutableLiveData
        todoList.observe(this, Observer {
            todayAdapter.itemList = it
            todayAdapter.notifyDataSetChanged()
        })

</code></pre>
 
 


### 느낀점
 - ViewModel과 LiveData를 이용하여 MVVM패턴으로 만들어보았는데 


