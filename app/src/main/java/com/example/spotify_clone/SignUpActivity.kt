package com.example.spotify_clone

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.spotify_clone.LocalDatabase.SqlUserEntity
import com.example.spotify_clone.LocalDatabase.UserLoginSignUpDatabase
import com.example.spotify_clone.ViewModels.SignupViewModel
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SignUpActivity : AppCompatActivity() {
  private lateinit var toolbar:androidx.appcompat.widget.Toolbar
  private lateinit var input:EditText
  private lateinit var passInput:EditText
  private lateinit var acknowledgement:TextView
  private lateinit var passAcknowledgement:TextView
  private lateinit var next:Button
  private lateinit var passNext:Button
  private lateinit var signup:LinearLayout
  private lateinit var passSignup:LinearLayout
  private lateinit var birthNext:Button
  private lateinit var birth:LinearLayout
  private lateinit var birthPicker:DatePicker
  private lateinit var viewModel:SignupViewModel
  private lateinit var genderGroup:ChipGroup
  private lateinit var gender:LinearLayout
  private lateinit var maleChip: Chip
  private lateinit var femaleChip: Chip
  private lateinit var nonBinaryChip: Chip
  private lateinit var username:LinearLayout
  private lateinit var usernameInput:EditText
  private lateinit var createAccount:Button
  private var curpos:Int=0

  enum class currentPosition{
    emial,password,birth,gender,username
  }
  val layouts:ArrayList<Int> = ArrayList(5)


  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_sign_up)
    initialiseViews()
    layouts.addAll(listOf(R.id.signup,R.id.password,R.id.birth,R.id.gender,R.id.username))
    viewModel=ViewModelProvider(this).get(SignupViewModel::class.java)
    setSupportActionBar(toolbar)
    setCurrentPosition(0)
    supportActionBar?.setHomeButtonEnabled(true)
    supportActionBar?.setTitle("")

    next.background=ContextCompat.getDrawable(this,R.drawable.next_button_error_bg)
    passNext.setBackgroundDrawable(ContextCompat.getDrawable(this,R.drawable.next_button_error_bg))
    input.addTextChangedListener{
      if(!input.text.isEmpty()){
        if(Patterns.EMAIL_ADDRESS.matcher(input.text).matches()){
          next.isEnabled=true
          next.setBackgroundDrawable(ContextCompat.getDrawable(this,R.drawable.next_button_bg))
          input.setBackgroundDrawable(ContextCompat.getDrawable(this,R.drawable.input_details_background))

          input.setTextColor(Color.WHITE)
        }else{
          next.setBackgroundDrawable(ContextCompat.getDrawable(this,R.drawable.next_button_error_bg))
          next.isEnabled=false
          input.setBackgroundDrawable(ContextCompat.getDrawable(this,R.drawable.input_details_error_background))
          input.setTextColor(Color.RED)
        }
      }
      else{
        next.isEnabled=false
        next.background=ContextCompat.getDrawable(this,R.drawable.next_button_bg)
        input.setTextColor(Color.GRAY)
        input.setBackgroundColor(resources.getColor(R.color.secondaryLightColor))
      }
    }
    passInput.addTextChangedListener{
      if(passInput.text.length<8){
        passAcknowledgement.setText("password should be greater than 8 characters")
        passNext.setBackgroundDrawable(ContextCompat.getDrawable(this,R.drawable.next_button_error_bg))
        passNext.isEnabled=false
      }else if(passInput.text.length>=8){
        passNext.isEnabled=true
        passNext.setBackgroundDrawable(ContextCompat.getDrawable(this,R.drawable.next_button_bg))
        passAcknowledgement.setText("")
      }else{
        passNext.setBackgroundDrawable(ContextCompat.getDrawable(this,R.drawable.next_button_error_bg))
      }
    }

    next.setOnClickListener {
        Log.d("next", "onCreate: checking email")
      viewModel.isEmailAlreadyExist(input.text.toString()).observe(this, Observer {
        if(it==false) {
          viewModel.setEmail(input.text.toString())
          setCurrentPosition(1)
          Log.d("next", "onCreate: email doesn't exist")
          proceedToPassword()
        }else{
          Log.d("next", "onCreate: email already exist")
          acknowledgement.text="This email already exists, Try using login"
        }
      })
    }
    passNext.setOnClickListener {
      setCurrentPosition(2)
      proceedToBirth()
    }
    birthNext.setOnClickListener {
      setCurrentPosition(3)
      viewModel.setBirthDate(birthPicker.dayOfMonth,birthPicker.month,birthPicker.year)
      proccedToGenderSelection()
    }



    genderGroup.setOnCheckedStateChangeListener(object :ChipGroup.OnCheckedStateChangeListener{
      override fun onCheckedChanged(group: ChipGroup, checkedIds: MutableList<Int>) {
        if(checkedIds.size>0) {
          val chip:Chip=findViewById(checkedIds.get(checkedIds.size-1))
          chip.chipStrokeColor= ColorStateList.valueOf(Color.WHITE)
          proccedToAccount(findViewById(checkedIds.get(checkedIds.size-1)))
        }
      }

    })

    createAccount.setOnClickListener {
      viewModel.setUsername(usernameInput.text.toString())

        viewModel.createUser().observe(this@SignUpActivity, Observer {
          Log.d("TAG", "onCreate: observer"+it)
          if(it!=null){
            val bundle=Bundle()
            val intent=Intent(this@SignUpActivity,SpotifyActivity::class.java)
            bundle.putParcelable(LoginActivity.USER,it)
            intent.putExtra(LoginActivity.USER,bundle)
            startActivity(intent)
            UserLoginSignUpDatabase.getInstance(this@SignUpActivity)?.getUserLoginDao()?.insertUser(SqlUserEntity(null,it.username,it.userRef!!))
          }
        })

    }
  }




  private fun proccedToGenderSelection() {
    animationHandler(birth,gender)
  }

  private fun proccedToAccount(view:View) {
    Log.d("TAG", "proccedToAccount: ")
    when(view.id){
      R.id.male_chip -> { viewModel.setGender("Male")
        Log.d("TAG", "proccedToAccount: male")
        findViewById<Chip>(view.id).chipStrokeColor= ColorStateList.valueOf(Color.WHITE)
        femaleChip.chipStrokeColor=ColorStateList.valueOf(resources.getColor(R.color.secondaryLightColor))
        nonBinaryChip.chipStrokeColor=ColorStateList.valueOf(resources.getColor(R.color.secondaryLightColor))
        setCurrentPosition(4)
        proceedToAccountCreation()
      }
      R.id.female_chip -> { viewModel.setGender("Female")
        Log.d("TAG", "proccedToAccount: female")
        findViewById<Chip>(view.id).chipStrokeColor= ColorStateList.valueOf(Color.WHITE)
        nonBinaryChip.chipStrokeColor=ColorStateList.valueOf(resources.getColor(R.color.secondaryLightColor))
        maleChip.chipStrokeColor=ColorStateList.valueOf(resources.getColor(R.color.secondaryLightColor))
        proceedToAccountCreation()
      }
      R.id.non_binary_chip -> { viewModel.setGender("Non-Binary")
        Log.d("TAG", "proccedToAccount: non binary")
        findViewById<Chip>(view.id).chipStrokeColor= ColorStateList.valueOf(Color.WHITE)
        maleChip.chipStrokeColor=ColorStateList.valueOf(resources.getColor(R.color.secondaryLightColor))
        femaleChip.chipStrokeColor=ColorStateList.valueOf(resources.getColor(R.color.secondaryLightColor))
        proceedToAccountCreation()
      }
    }
  }

  private fun proceedToAccountCreation() {
    animationHandler(gender,username)
  }

  private fun proceedToBirth() {
    viewModel.setPassword(passInput.text.toString())
    animationHandler(passSignup,birth)
  }

  private fun proceedToPassword() {
    animationHandler(signup,passSignup)
  }
  private fun animationHandler(remLayout:LinearLayout,newLayout: LinearLayout){

    newLayout.visibility=View.VISIBLE
    val slideOut=AnimationUtils.loadAnimation(this,R.anim.old_slide_in_animation)
    val slideIn=AnimationUtils.loadAnimation(this,R.anim.slide_in_animation)
    remLayout.startAnimation(slideOut)
    slideOut.setAnimationListener(object:Animation.AnimationListener{
      override fun onAnimationStart(animation: Animation?) { newLayout.startAnimation(slideIn) }

      override fun onAnimationEnd(animation: Animation?) { remLayout.visibility= View.GONE }

      override fun onAnimationRepeat(animation: Animation?) { }
    })
  }

  private fun setCurrentPosition(pos:Int) {
    this.curpos=pos
  }
  private fun reverseAnimation(remLayout:LinearLayout,newLayout: LinearLayout){

    val oldSlideOut=AnimationUtils.loadAnimation(this,R.anim.old_slide_out_animation)
    val slideOut=AnimationUtils.loadAnimation(this,R.anim.slide_out_animation)
    remLayout.startAnimation(slideOut)
    slideOut.setAnimationListener(object:Animation.AnimationListener{
      override fun onAnimationStart(animation: Animation?) {
        newLayout.visibility=View.VISIBLE
        newLayout.startAnimation(oldSlideOut)
      }

      override fun onAnimationEnd(animation: Animation?) { remLayout.visibility= View.GONE }

      override fun onAnimationRepeat(animation: Animation?) { }
    })
  }
  private fun reverseAnimationHandler(): Boolean {
    var def=true
    if(curpos>0){
      reverseAnimation(findViewById(layouts.get(curpos)), findViewById(layouts.get(curpos-1)))
      curpos--
      def=false
    }
    return def
  }


  override fun onBackPressed() {
    if(reverseAnimationHandler()){
      super.onBackPressed()
    }
  }
  private fun initialiseViews() {
    toolbar=findViewById(R.id.signup_toolbar)

    signup=findViewById(R.id.signup)
    input=findViewById(R.id.email)
    acknowledgement=findViewById(R.id.acknowledgement)
    next=findViewById(R.id.next)
    next.isEnabled=false

    passSignup=findViewById(R.id.password)
    passInput=findViewById(R.id.pass_take_details)
    passAcknowledgement=findViewById(R.id.pass_acknowledgement)
    passNext=findViewById(R.id.pass_next)
    passNext.isEnabled=false

    birth=findViewById(R.id.birth)
    birthNext=findViewById(R.id.birth_next)
    birthPicker=findViewById(R.id.birth_picker)

    gender=findViewById(R.id.gender)
    genderGroup=findViewById(R.id.gender_group)
    maleChip=findViewById(R.id.male_chip)
    femaleChip=findViewById(R.id.female_chip)
    nonBinaryChip=findViewById(R.id.non_binary_chip)

    maleChip.chipStrokeColor=ColorStateList.valueOf(resources.getColor(R.color.secondaryLightColor))
    femaleChip.chipStrokeColor=ColorStateList.valueOf(resources.getColor(R.color.secondaryLightColor))
    nonBinaryChip.chipStrokeColor=ColorStateList.valueOf(resources.getColor(R.color.secondaryLightColor))
    maleChip.chipBackgroundColor=ColorStateList.valueOf(Color.TRANSPARENT)
    femaleChip.chipBackgroundColor=ColorStateList.valueOf(Color.TRANSPARENT)
    nonBinaryChip.chipBackgroundColor=ColorStateList.valueOf(Color.TRANSPARENT)

    username=findViewById(R.id.username)
    usernameInput=findViewById(R.id.username_take_details)
    createAccount=findViewById(R.id.create_account)


  }
}