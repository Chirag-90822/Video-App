package com.example.android


import android.annotation.SuppressLint
import android.app.Dialog
import android.content.ContentResolver
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.database.Cursor
import android.graphics.Color
import android.graphics.Point
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.AudioManager
import android.media.MediaMetadataRetriever
import android.media.MediaScannerConnection
import android.media.audiofx.AudioEffect
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.util.TypedValue
import android.view.*
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GestureDetectorCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide.*
import com.developer.filepicker.controller.DialogSelectionListener
import com.developer.filepicker.model.DialogConfigs
import com.developer.filepicker.model.DialogProperties
import com.developer.filepicker.view.FilePickerDialog
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.SimpleExoPlayer.*
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.MergingMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.SingleSampleMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.*
import com.google.android.exoplayer2.upstream.*
import com.google.android.exoplayer2.util.Assertions
import com.google.android.exoplayer2.util.MimeTypes
import com.google.android.exoplayer2.util.Util
import java.io.File
import java.net.URLDecoder
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList
import android.provider.MediaStore
import android.view.KeyEvent.FLAG_LONG_PRESS
import com.example.testing.VideoDatabase
import com.example.testing.VideoFile
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.security.Key


class VideoPlay : AppCompatActivity(), ScaleGestureDetector.OnScaleGestureListener,
    View.OnTouchListener,SensorEventListener {
    var allow=true
    var position= String()

    lateinit var uri3:Uri
    val threshold=100
    lateinit var acclerometer: Sensor
    lateinit var sensorManage: SensorManager
    var changing=FilesActivity()

    var calculatedTime:Int = 0
    lateinit var dialogproperties:DialogProperties
    lateinit var filePickerDialog: FilePickerDialog
    lateinit var uriSubtitle:Uri
    private  val MEDIA_VIDEO_MIME = "video/*"
     var databaseid:Long = 0.toLong()
    lateinit var videoDatabase2:VideoDatabase

    var islock=true
    var oldtimestamp:Long=0
    var newtimestamp:Long=0
    var end=false
    var expand=false
    var movedown=false
    var backsubtitlesize=18f
    lateinit var scaling:ImageView
    lateinit var brighttext:TextView
    lateinit var volmetext:TextView
    lateinit var overlay:View
    var moveup=false
    var isenable=true
    var arrayList: ArrayList<Float> = ArrayList()
    var list: MutableList<Float> = mutableListOf()
    private var mVelocityTracker: VelocityTracker? = null

    var ok:ArrayList<model> = ArrayList()
     lateinit var aspect:RelativeLayout
    private var sWidth = 0
    private  var sHeight:Int = 0
    private var baseX = 0f
    var subtitlesize=0f
    lateinit var audio: AudioManager
    var currentVolume =0
    lateinit var trackSelector:DefaultTrackSelector

    lateinit var progress:RelativeLayout

    lateinit var bottomicons:LinearLayout
    var maxVolume=0
    private  var baseY:Float = 0f
    private var diffX: Long = 0
    private  var diffY:Long = 0
    var bright :Float=0f
    lateinit var seekcurrtext:TextView
    lateinit var seektext:TextView
    lateinit var seek:LinearLayout
    var curent=0

    lateinit var parameters:PlaybackParameters

    private var tested_left = false
    private var tested_right=false

    private  var intLeft:kotlin.Boolean = false
    private  var intRight:kotlin.Boolean = false
    var anotherdiffy=0.0
    private  var intTop:kotlin.Boolean = false
    private  var intBottom:kotlin.Boolean = false
    private  var moveleft:kotlin.Boolean = true
    private  var moveright:kotlin.Boolean = true
    private  var finTop:kotlin.Boolean = false
    private  var finBottom:kotlin.Boolean = false
    private val MIN_DISTANCE = 150
    private val cResolver: ContentResolver? = null
    lateinit var root_layo:RelativeLayout
     lateinit var brightprogress:SeekBar
     lateinit var brightness:LinearLayout
     var volumecalcalculation:Double=0.0
     lateinit var volume:LinearLayout
     lateinit var volumeprogress:SeekBar
    lateinit var exo_progress:DefaultTimeBar
    lateinit var concatenate:ConcatenatingMediaSource
    lateinit var whichfol:String
    private var scale = 1.0f
    var checkeditem=1
    var numbersub=0
    var seekdurme=0.toLong()
    lateinit var scaleDetector:ScaleGestureDetector
    var iconmodelarraylist = ArrayList<IconModel>()
    lateinit var recyclericons:RecyclerView
    lateinit var playbackiconAdapter:PlaybackiconAdapter

     lateinit var gestureDetector:GestureDetectorCompat
    lateinit var simplexo:ExoPlayer
    lateinit var playerview:PlayerView
    var brightcalculation:Double=0.0
    lateinit var toolbar:androidx.appcompat.widget.Toolbar
    var comingintent=false
    var ismediafile=false
    lateinit var arrayintent:ArrayList<model>
   lateinit var folname:File
   lateinit var volumeicon:ImageView

    override fun onCreate(savedInstanceState: Bundle?) {

        window.setNavigationBarColor(getResources().getColor(R.color.black))
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.attributes.layoutInDisplayCutoutMode=WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES

        }


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_play)


        videoDatabase2= VideoDatabase.getDatabase(this)
        this.window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        var ti=getIntent().getStringExtra("foldername").toString()
        when {
            intent?.action == Intent.ACTION_VIEW -> {
                handleSendVideo(intent)
                comingintent=true

            }
        }
      if(comingintent==false){
        position=getIntent().getIntExtra("positio",0).toString()
        Log.d("fuck","ooap "+position)
        ok= getIntent().getExtras()!!.getParcelableArrayList<model>("videolist") as ArrayList<model>
        var pu=ok.get(position.toInt()).path}
        else{


          var comei=intent.data.toString()
          var seck=comei.lastIndexOf("/")
          var iop=URLDecoder.decode(comei.substring(seck+1))
          whichfol=iop
          if(ismediafile){
              ismediafile=false
          val cursor: Cursor? = this.getContentResolver().query(intent.data!!, null, null, null, null)
          if (cursor!!.moveToFirst()) {
              val column_index: Int =
                  cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA)

              var filePathUri = (cursor.getString(column_index))
              var fillast=(filePathUri.lastIndexOf("/"))
              var newfill=filePathUri.substring(fillast+1)
              iop=newfill
          whichfol=iop}


          }
          Log.d("makan","pooj "+iop)
          var il=0
          for(jop in ok){
              if(jop.title==iop){
                  il=ok.indexOf(jop)
              }
          }
          position=il.toString()

          folname=File(ok.get(position.toInt()).path)


          databaseintent(ok,position,folname)




      }

        var display = windowManager.defaultDisplay
        var size = Point()
        display.getSize(size)
        sWidth = size.x
        sHeight = size.y







        playerview=findViewById(R.id.exoplayer)
        brightness=findViewById(R.id.brightness_slider_container)
        brightprogress=findViewById(R.id.brightness_slider)


         root_layo=findViewById(R.id.root_layout)
         volume=findViewById(R.id.Volume_slider_container)
        volumeprogress=findViewById(R.id.Volume_slider)
        seekcurrtext=findViewById(R.id.txt_seek_currTime)
        seektext=findViewById(R.id.txt_seek_secs)
        seek=findViewById(R.id.seekbar_center_text)
        bottomicons=findViewById(R.id.bottom_icon)
        progress=findViewById(R.id.progress)
        recyclericons=findViewById(R.id.recycler_icons)
        volumeicon=findViewById(R.id.VolumeIcon)

        aspect=findViewById(R.id.opl)
        val relative=findViewById<RelativeLayout>(R.id.root)
        bright =
            Settings.System.getFloat(contentResolver, Settings.System.SCREEN_BRIGHTNESS, -1f)
        val layout = window.attributes
        layout.screenBrightness = bright / 255
        brightcalculation=((bright.toDouble() / 255) * 100)
        getWindow().setAttributes(layout)
        val boi=
        brightprogress.setProgress(((((bright / 255) * 100)/100)*15).toInt())

        audio = this.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        currentVolume = audio.getStreamVolume(AudioManager.STREAM_MUSIC)
        maxVolume = audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        volumecalcalculation=(Math.ceil((currentVolume.toDouble()/maxVolume.toDouble())*100))
        volumeprogress.setProgress(currentVolume)
        volmetext=findViewById(R.id.volumetext)
        brighttext=findViewById(R.id.brighttext)
        volmetext.setText((volumeprogress.progress.toString()))
        brighttext.setText(brightprogress.progress.toString())


        relative.setOnTouchListener(this)

        var captionStyleCompat = CaptionStyleCompat(Color.WHITE,Color.TRANSPARENT,Color.TRANSPARENT,CaptionStyleCompat.EDGE_TYPE_DROP_SHADOW,Color.TRANSPARENT,null)

        playerview.getSubtitleView()!!.setStyle(captionStyleCompat)
playerview.subtitleView!!.setApplyEmbeddedStyles(false)
        playerview.subtitleView!!.setFixedTextSize(TypedValue.COMPLEX_UNIT_SP,18f)
        val forward=findViewById<ImageView>(R.id.exo_ffwd_with_amount)
        exo_progress=findViewById(R.id.exo_progress)
        var go_back=findViewById<ImageView>(R.id.go_back)
        var rewind=findViewById<ImageView>(R.id.exo_rew_with_amount)
        var unlock=findViewById<ImageView>(R.id.openlock)
        var lock=findViewById<ImageView>(R.id.lock)
        var prev=findViewById<ImageView>(R.id.exo_prev)
        var next=findViewById<ImageView>(R.id.exo_next)
        toolbar=findViewById(R.id.movie_title)


        var movie=findViewById<TextView>(R.id.movie_logo)
        scaling=findViewById<ImageView>(R.id.video_screen)
        scaling.setOnClickListener(firstlistener)
        nointent(ok,position.toInt())





        go_back.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                onBackPressed()
            }

        })
        movie.setText(ok.get(position.toInt()).title)
        rewind.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                simplexo.seekTo(simplexo.currentPosition-10000)
            }

        })
        prev.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
               if(position.toInt()>0){

                   var lolliop=TimeUnit.MILLISECONDS.toSeconds(simplexo.currentPosition)
                   GlobalScope.launch {
                       if(videoDatabase2.fileDao().iscount().size>0){
                           var know=videoDatabase2.fileDao().updatedur(lolliop,databaseid,0)
                           Log.d("cheezyone","prev ")}
                   }

                   var changevideo=position.toInt()
                   changevideo-=1
                   position=changevideo.toString()
                   movie.setText(ok.get(changevideo).title)
                   simplexo.stop()
                   if(comingintent){
                       databaseintent(ok,changevideo.toString(),folname)
                   }
                   else{
                       changing.changenew(ok.get(position.toInt()).title!!)
                       nointent(ok,changevideo)
                   }

                   playvideo()
               }else{
                   Toast.makeText(this@VideoPlay,"No previous video to play",Toast.LENGTH_SHORT).show()
               }
            }

        })
        next.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {

                if(position.toInt()<ok.size){
                    var lolliop=TimeUnit.MILLISECONDS.toSeconds(simplexo.currentPosition)
                    GlobalScope.launch {
                        if(videoDatabase2.fileDao().iscount().size>0){
                            var know=videoDatabase2.fileDao().updatedur(lolliop,databaseid,0)
                            Log.d("cheezyone","next "+know)}
                    }
                    var changevideo=position.toInt()
                    changevideo+=1
                    position=changevideo.toString()
                    movie.setText(ok.get(changevideo).title)
                      simplexo.stop()
                    if(comingintent){
                        databaseintent(ok,changevideo.toString(),folname)
                    }
                    else{
                        changing.changenew(ok.get(position.toInt()).title!!)
                        nointent(ok,changevideo)
                    }

                    playvideo()
                }else{

                    Toast.makeText(this@VideoPlay,"No Next video to play",Toast.LENGTH_SHORT).show()
                }
            }

        })
        lock.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {

                lock.visibility=View.INVISIBLE
                islock=true
                showDefaultControls()

            }

        })
        unlock.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                hideDefaultControls()
                lock.visibility=View.VISIBLE
                islock=false
            }

        })
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)








        dialogproperties= DialogProperties()
        filePickerDialog= FilePickerDialog(this)
        filePickerDialog.setPositiveBtnName("OK")
        filePickerDialog.setTitle("Select a Subtitle File")
        filePickerDialog.setNegativeBtnName("Cancel")


        sensorManage=getSystemService(Context.SENSOR_SERVICE) as SensorManager
        if(sensorManage.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)!=null){
            acclerometer=sensorManage.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)


        }else{

            Toast.makeText(this,"noy ",Toast.LENGTH_SHORT).show()
        }


        iconmodelarraylist.add(IconModel((R.drawable.ic_round_keyboard_arrow_right),""))
        iconmodelarraylist.add(IconModel((R.drawable.audiotrack),"Audio Track"))
        iconmodelarraylist.add(IconModel((R.drawable.ic_round_subtitles),"Subtitles"))
        iconmodelarraylist.add(IconModel((R.drawable.ic_round_screen_rotation),"Rotate"))
        playbackiconAdapter= PlaybackiconAdapter(iconmodelarraylist,this)
        recyclericons.adapter=playbackiconAdapter
        recyclericons.layoutManager=
            LinearLayoutManager(this,RecyclerView.HORIZONTAL,true)
        playbackiconAdapter.notifyDataSetChanged()


        playbackiconAdapter.setOnItemClickListener(object : PlaybackiconAdapter.OnItemClickListener{
            @SuppressLint("ResourceType")
            override fun onItemClick(position: Int) {
                super.onItemClick(position)
                if(position==0){
                   if(expand){
                       iconmodelarraylist.clear()
                       iconmodelarraylist.add(IconModel((R.drawable.ic_round_keyboard_arrow_right),""))
                       iconmodelarraylist.add(IconModel((R.drawable.audiotrack),"Audio Track"))
                       iconmodelarraylist.add(IconModel((R.drawable.ic_round_subtitles),"Subtitles"))
                       iconmodelarraylist.add(IconModel((R.drawable.ic_round_screen_rotation),"Rotate"))
                       playbackiconAdapter.notifyDataSetChanged()

                       expand=false
                   }else{
                       if(iconmodelarraylist.size==4){

                           iconmodelarraylist.add(IconModel((R.drawable.hplib_volume),"Volume"))
                           iconmodelarraylist.add(IconModel((R.drawable.ic_round_equalizer),"Equalizer"))
                           iconmodelarraylist.add(IconModel((R.drawable.ic_round_fast_forward),"Playback Speed"))
                           iconmodelarraylist.add(IconModel((R.drawable.ic_subtitle_position),"Customize Subtitle"))
                       }
                       iconmodelarraylist.set(position,IconModel((R.drawable.ic_round_keyboard_arrow_left),""))
                       playbackiconAdapter.notifyDataSetChanged()

                       expand=true
                   }
                }
                if(position==1){

                    try{

                        var mapped=Assertions.checkNotNull(trackSelector.currentMappedTrackInfo)
                        var render=0
                        for(i in 0..mapped.rendererCount-1){
                            var tracktype=mapped.getRendererType(i)
                            if(tracktype==C.TRACK_TYPE_AUDIO){
                                render=i
                                break;
                            }
                        }

                        var trackselection=TrackSelectionDialogBuilder(this@VideoPlay,"Audio Track",trackSelector,render)
                        trackselection.setTheme(R.style.CustomDialog)
                        trackselection.build().show()

                    }catch (e:Exception){
                        Toast.makeText(applicationContext,"Loading Video",Toast.LENGTH_SHORT).show()
                    }
                }
                if(position==2){
                    try{

                        var mapped=Assertions.checkNotNull(trackSelector.currentMappedTrackInfo)
                        var render=0
                        for(i in 0..mapped.rendererCount-1){
                            var tracktype=mapped.getRendererType(i)
                            if(tracktype==C.TRACK_TYPE_TEXT){
                                render=i
                                break;
                            }
                        }

                        var trackselection=TrackSelectionDialogBuilder(this@VideoPlay,"Subtitles",trackSelector,render)
                        trackselection.setTheme(R.style.CustomDialog)

                        trackselection.build().show()


                    }catch (e:Exception){
                        Toast.makeText(applicationContext,"Loading Video",Toast.LENGTH_SHORT).show()
                    }

                }
                if(position==3){

                    val orientation = resources.configuration.orientation
                    if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                        //set in landscape
                        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                    } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        //set in portrait
                        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                    }
                }
                if(position==4){


                }
                if(position==5){
                    val intent=Intent(AudioEffect.ACTION_DISPLAY_AUDIO_EFFECT_CONTROL_PANEL)
                    if((intent.resolveActivity(packageManager)!=null)){
                        startActivity(intent)
                    }else{
                        Toast.makeText(applicationContext,"No Equalizer Found",Toast.LENGTH_SHORT ).show()
                    }
                }
                if(position==6){
                    var alertdialog=AlertDialog.Builder(this@VideoPlay,R.style.CustomDialog)
                    alertdialog.setTitle("Playback Speed").setPositiveButton("ok",null)
                    var items= arrayOf("0.5x","1x Normal Speed","1.25x","1.5x","1.75x","2x")
                    var speed:Float= (-1.0).toFloat()


                    alertdialog.setSingleChoiceItems(items,checkeditem,object : DialogInterface.OnClickListener{
                        override fun onClick(dialog: DialogInterface?, which: Int) {
                            when(which){
                                0->{
                                 speed=0.5.toFloat()
                                    parameters= PlaybackParameters(speed)
                                    simplexo.playbackParameters=parameters
                                    checkeditem=0

                                }
                                1->{
                                    speed=1.0.toFloat()
                                    parameters= PlaybackParameters(speed)
                                    simplexo.playbackParameters=parameters
                                    checkeditem=1
                                }
                                2->{
                                    speed=1.25.toFloat()
                                    parameters= PlaybackParameters(speed)
                                    simplexo.playbackParameters=parameters
                                    checkeditem=2
                                }
                                3->{
                                    speed=1.5.toFloat()
                                    parameters= PlaybackParameters(speed)
                                    simplexo.playbackParameters=parameters
                                    checkeditem=3
                                }
                                4->{
                                    speed=1.75.toFloat()
                                    parameters= PlaybackParameters(speed)
                                    simplexo.playbackParameters=parameters
                                    checkeditem=4
                                }
                                5->{
                                    speed=2.toFloat()
                                    parameters= PlaybackParameters(speed)
                                    simplexo.playbackParameters=parameters
                                    checkeditem=5

                                }


                            }
                        }

                    })
                    var alert=alertdialog.create()

                    alert.show()
                }
                if(position==7){
                    val dialog=Dialog(this@VideoPlay)
                    dialog.setContentView(R.layout.custom_dialog)
                    dialog.window!!.setBackgroundDrawableResource(R.drawable.background)

                    var add=dialog.findViewById<ImageView>(R.id.add)
                    var sub=dialog.findViewById<ImageView>(R.id.sub)
                    var number=dialog.findViewById<TextView>(R.id.numbers)
                    var oki=dialog.findViewById<TextView>(R.id.okay)
                    var add1=dialog.findViewById<ImageView>(R.id.add1)
                    var sub1=dialog.findViewById<ImageView>(R.id.sub1)
                    var number1=dialog.findViewById<TextView>(R.id.numberboi)
                    number.setText(numbersub.toString())
                    number1.setText(subtitlesize.toString())
                    add.setOnClickListener(object : View.OnClickListener{
                        override fun onClick(v: View?) {
                            if(numbersub<11){
                         var trans= playerview.subtitleView!!.translationY

                            playerview.subtitleView!!.translationY=trans-10
                            numbersub+=1
                            number.setText(numbersub.toString())}
                        }

                    })
                    sub.setOnClickListener(object :View.OnClickListener{
                        override fun onClick(v: View?) {
                            if(numbersub>-11){
                            var trans= playerview.subtitleView!!.translationY

                            playerview.subtitleView!!.translationY=trans+10
                            numbersub-=1
                            number.setText(numbersub.toString())}
                        }

                    })
                    oki.setOnClickListener(object : View.OnClickListener{
                        override fun onClick(v: View?) {
                            dialog.dismiss()
                        }

                    })

                    add1.setOnClickListener(object: View.OnClickListener {
                        override fun onClick(v: View?) {
                         if(10>subtitlesize){
                            var ogsize=backsubtitlesize+1
                            backsubtitlesize=ogsize

                           playerview.subtitleView!!.setFixedTextSize(TypedValue.COMPLEX_UNIT_SP,ogsize)
                            subtitlesize+=1
                            number1.setText(subtitlesize.toString())}
                            else{
                             Toast.makeText(applicationContext,"Size cannot be increased",Toast.LENGTH_SHORT).show()
                         }
                        }

                    })
                    sub1.setOnClickListener(object : View.OnClickListener{
                        override fun onClick(v: View?) {
                            if(-10<subtitlesize){
                            var ogsize=(backsubtitlesize-1).toFloat()
                            playerview.subtitleView!!.setFixedTextSize(TypedValue.COMPLEX_UNIT_SP,ogsize)
                            subtitlesize-=1
                            backsubtitlesize=ogsize
                            number1.setText(subtitlesize.toString())}
                            else{
                                Toast.makeText(applicationContext,"Size cannot be decreased",Toast.LENGTH_SHORT).show()
                            }
                        }

                    })


                    dialog.show()


                }
            }
        })

        playvideo()

forward.setOnClickListener(object :View.OnClickListener{
    override fun onClick(v: View?) {
        simplexo.seekTo(simplexo.contentPosition+10000)
    }

})


        scaleDetector = ScaleGestureDetector(applicationContext, this)
        gestureDetector= GestureDetectorCompat(applicationContext,object :
                      GestureDetector.SimpleOnGestureListener() {
            override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
                if(isenable&&islock){
                    hideDefaultControls()
                    isenable=false

                }
                else if(islock){

                  showDefaultControls()
                    isenable=true

                }
                if(islock==false){
                    if(lock.isVisible){
                        if(playerview.isControllerVisible==false){
                            playerview.showController()
                            lock.visibility=View.INVISIBLE
                        }else{
                            lock.visibility=View.INVISIBLE
                        }

                    }else{
                        if(playerview.isControllerVisible==false){
                            playerview.showController()
                            lock.visibility=View.VISIBLE
                        }else{
                            lock.visibility=View.VISIBLE
                        }
                    }
                }


                return true
            }


            override fun onDown(e: MotionEvent?): Boolean {
                return true
            }




        })






    }

    private fun nointent(ok5: ArrayList<model>, changevideo5: Int) {
        GlobalScope.launch {
            if(videoDatabase2.fileDao().iscount().size>0&&comingintent==false){
                val op10=File(ok5.get(changevideo5).path)
                val datalist=videoDatabase2.fileDao().kop2(ok5.get(changevideo5).title!!,op10.parentFile.name)


                for(f in datalist){
                    databaseid=f.id
                    seekdurme=TimeUnit.SECONDS.toMillis(f.duration)
                }

            }
        }

    }

    private fun databaseintent(ok1: ArrayList<model>, position2: String, folname: File) {
        arrayintent =ArrayList<model>()
        var lpook=MediaMetadataRetriever()
        lpook.setDataSource(ok1.get(position2.toInt()).path)
        var duracel=lpook.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
        arrayintent.add(model(ok1.get(position2.toInt()).title,ok1.get(position2.toInt()).path,duracel.toString()))

        GlobalScope.launch {


            var myown=arrayintent.iterator()
            while(myown.hasNext()){
                var newopp=myown.next()
                var durme=(newopp.duration)!!.toLong()
                if(videoDatabase2.fileDao().isthere34(newopp.title!!)==false){
                    var identify=File(newopp.path).lastModified()
                videoDatabase2.fileDao().insertvideo(VideoFile((0.toLong()),
                    newopp.title!!,(0.toLong()),newopp.path!!,0,folname.parentFile.name,identify))


                Log.d("cheezyone","aarahai "+false)}
                val datalist=videoDatabase2.fileDao().kop2(ok1.get(position2.toInt()).title!!,folname.parentFile.name)
                for(da in datalist){
                    databaseid=da.id
                    seekdurme=TimeUnit.SECONDS.toMillis(da.duration)
                }
                Log.d("cheezyone","aar "+seekdurme)


            }
        }


    }

    private fun handleSendVideo(intent: Intent) {

            comingintent=true
            var comeon=intent.data
            var comei=intent.data.toString()
            var seck=comei.lastIndexOf("/")
            var iop=comei.substring(0,seck)
            var lopo=URLDecoder.decode(iop.substring(iop.lastIndexOf("/")+1))


        if(lopo=="file"||lopo=="media"){
            ismediafile=true
            val cursor: Cursor? = this.getContentResolver().query(comeon!!, null, null, null, null)
            if (cursor!!.moveToFirst()) {
                val column_index: Int =
                    cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA)

                var filePathUri = (cursor.getString(column_index))
                var fillast=filePathUri.substring(0,filePathUri.lastIndexOf("/"))
                var newfill=fillast.substring(fillast.lastIndexOf("/")+1)
                lopo=newfill


            }

        }
        val folderarray= fetch(Environment.getExternalStorageDirectory(),lopo)




    }

    private fun fetch(storageDirectory: File?, lopo: String) :ArrayList<String>{
        val all = arrayListOf<String>()
        val video=storageDirectory!!.listFiles();


        val s=storageDirectory.parentFile.name

        video.forEach { f ->
            if(!f.isHidden&&f.isDirectory && s!="Android"){
                all.addAll(fetch(f,lopo));
            }
            else{
                if((f.name.endsWith(".mp4")||f.name.endsWith(".mkv"))){
                    val pname=f.parentFile.name
                    if(pname==lopo){
                        ok.add(model(f.name,f.path,"0.00"))

                    }




                    }

                }
            }
        return all
        }




    private fun playvideo() {
        simplexo=ExoPlayer.Builder(this,DefaultRenderersFactory(this)).build()
        val datasource=DefaultDataSourceFactory(this,Util.getUserAgent(this,"app"))
        concatenate=ConcatenatingMediaSource()
        for(i in ok.indices){

            File(ok.get(i).toString())
            val mediasource=ProgressiveMediaSource.Factory(datasource).createMediaSource(Uri.parse(ok.get(position.toInt()).path))
            concatenate.addMediaSource(mediasource)


        }
        playerview.setPlayer(simplexo)
        simplexo.prepare(concatenate)

        simplexo.seekTo(position.toInt(),C.TIME_UNSET)
        playError()

    }

    private fun playError() {
        simplexo.addListener(object: Player.EventListener{
            override fun onPlayerError(error: PlaybackException) {
                Toast.makeText(this@VideoPlay,"Error",Toast.LENGTH_SHORT).show()
            }

            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                super.onPlayerStateChanged(playWhenReady, playbackState)
                if (playbackState == Player.STATE_IDLE || playbackState == Player.STATE_ENDED ||
                    !playWhenReady) {

                    playerview.setKeepScreenOn(false);
                } else { // STATE_IDLE, STATE_ENDED
                    // This prevents the screen from getting dim/lock
                    playerview.setKeepScreenOn(true)
                }
            }
        })


        if(seekdurme<=0){
            simplexo.play()
            Log.d("cheezyone","pooiij "+seekdurme)
        }else{
            simplexo.seekTo(seekdurme)
            Log.d("cheezyone","pooi "+seekdurme)
            simplexo.play()

        }



        trackSelector= simplexo.trackSelector as DefaultTrackSelector
    }


    override fun onScale(detector: ScaleGestureDetector?): Boolean {
         val scaleFactor = scaleDetector.getScaleFactor()
        allow=false
         scale *= scaleFactor;
         scale = Math.max(1.0f, Math.min(scale, 5.0f))
         aspect.scaleX=scale
         aspect.scaleY=scale

         return true
     }

     override fun onScaleBegin(detector: ScaleGestureDetector?): Boolean {
         allow=false
         return true
     }

     override fun onScaleEnd(detector: ScaleGestureDetector?) {
           allow=true
     }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        scaleDetector.onTouchEvent(event)
     gestureDetector.onTouchEvent(event)
        when (event!!.actionMasked) {

            MotionEvent.ACTION_DOWN -> {
                curent= simplexo.currentPosition.toInt()
                if(resources.configuration.orientation==Configuration.ORIENTATION_PORTRAIT) {
                    if (event.getX() < (sWidth / 2)) {
                        intLeft = true;
                        intRight = false;
                        Log.d("width", "op1 " + sWidth / 2)
                    } else if (event.getX() > (sWidth / 2)) {
                        intLeft = false;
                        intRight = true;
                        Log.d("width", "op " + sWidth / 2)
                    }
                }else if(resources.configuration.orientation==Configuration.ORIENTATION_LANDSCAPE){
                    if (event.getX() < (sHeight / 2)) {
                        intLeft = true;
                        intRight = false;
                        Log.d("width", "land1 " + sHeight / 2)
                    } else if (event.getX() > (sHeight / 2)) {
                        intLeft = false;
                        intRight = true;
                        Log.d("width", "land2 " + sHeight / 2)
                    }
                }


                oldtimestamp=System.currentTimeMillis()
                // Reset the velocity tracker back to its initial state.
                mVelocityTracker?.clear()
                // If necessary retrieve a new VelocityTracker object to watch the
                // velocity of a motion.
                mVelocityTracker = mVelocityTracker ?: VelocityTracker.obtain()
                // Add a user's movement to the tracker.
                mVelocityTracker?.addMovement(event)
                 baseX=event.getX()
                baseY=event.getY()
            }
            MotionEvent.ACTION_MOVE -> {
                diffX = Math.ceil((event.x - baseX).toDouble()).toLong()
                diffY = Math.ceil((event.y - baseY).toDouble()).toLong()
                newtimestamp=System.currentTimeMillis()
               var timediff=(newtimestamp-oldtimestamp)

                mVelocityTracker?.addMovement(event)
                mVelocityTracker?.computeCurrentVelocity(1000)
                if (allow == true && islock){
                    if (Math.abs(diffY) > Math.abs(diffX)&& Math.abs(diffY)>100&&seek.visibility==View.GONE) {
                        if (intLeft) {
                            tested_left=true
                            var velop = Math.sqrt((mVelocityTracker!!.getXVelocity() * mVelocityTracker!!.getXVelocity() + mVelocityTracker!!.getYVelocity() * mVelocityTracker!!.getYVelocity()).toDouble())
                                brightness.visibility=View.VISIBLE
                            Log.d("likop","down "+velop)
                            if(diffY>0){
                                if(velop<1000 && brightcalculation>=0){
                                    var conversion=velop/1000
                                    if(conversion<0.4){
                                        brightcalculation=brightcalculation-0.4

                                        Log.d("consuming","down "+brightcalculation)
                                    }else{
                                        var io=conversion*10
                                        brightcalculation=brightcalculation-io

                                        Log.d("consuming","down1 "+brightcalculation+" "+io)

                                    }
                                }
                                else if(brightcalculation>=0){

                                    brightcalculation=brightcalculation-TimeUnit.MILLISECONDS.toSeconds(velop.toLong())
                                }

                              if(brightcalculation>=0) {
                                  movedown=true
                                  brightprogress.setProgress(((brightcalculation / 100) * 15).toInt())
                                  val bru = (brightcalculation.toFloat() / 100.0)
                                  val layoutpars: WindowManager.LayoutParams =
                                      window.getAttributes()
                                  layoutpars.screenBrightness = bru.toFloat()
                                  window.setAttributes(layoutpars)
                                  brighttext.setText(brightprogress.progress.toString())

                              }
                                else{
                                    movedown=true
                                    brightprogress.setProgress(0)
                                  brighttext.setText("0")
                              }
                            }else{
                                if(velop<1000 && brightcalculation<=100){
                                    var conversion=velop/1000
                                    if(conversion<0.4){
                                        brightcalculation=brightcalculation+0.4

                                        Log.d("consuming","up "+brightcalculation)
                                    }else{
                                        var io=conversion*10
                                        brightcalculation=brightcalculation+io

                                        Log.d("consuming","up1 "+brightcalculation+" "+io)

                                    }
                                }

                                else if(brightcalculation<=100){

                                    brightcalculation=brightcalculation+TimeUnit.MILLISECONDS.toSeconds(velop.toLong())
                                }
                              if(brightcalculation<=100) {
                                  moveup=true
                                  brightprogress.setProgress(((brightcalculation / 100) * 15).toInt())
                                  val bru = (brightcalculation.toFloat() / 100.0)
                                  val layoutpars: WindowManager.LayoutParams =
                                      window.getAttributes()
                                  layoutpars.screenBrightness = bru.toFloat()
                                  window.setAttributes(layoutpars)
                                  brighttext.setText(brightprogress.progress.toString())
                              }
                                else{
                                    moveup=true
                                    brightprogress.setProgress(15)
                                  brighttext.setText("15")
                                }

                            }
                            if(moveup && anotherdiffy<diffY){
                                baseY= event.getY()
                                baseX=event.getX()
                                moveup=false
                            }else if(movedown && anotherdiffy>diffY){
                                movedown=false
                                baseX=event.getX()
                                baseY=event.getY()
                            }
                            anotherdiffy=diffY.toDouble()


                            Log.d("leftpart","pdojdo ")

                        }else if(intRight){
                            tested_right=true

                            volume.visibility=View.VISIBLE
                            var maxVol=audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC)

                            var velop = Math.sqrt((mVelocityTracker!!.getXVelocity() * mVelocityTracker!!.getXVelocity() + mVelocityTracker!!.getYVelocity() * mVelocityTracker!!.getYVelocity()).toDouble())



                            if(diffY>0){
                                if(velop<1000 && volumecalcalculation>=0){
                                    var conversion=velop/1000
                                    if(conversion<0.4){
                                        volumecalcalculation=volumecalcalculation-0.5
                                    }else{
                                        var io=conversion*10
                                        volumecalcalculation=volumecalcalculation-io



                                    }
                                }

                                else if(volumecalcalculation>=0){
                                    volumecalcalculation=volumecalcalculation-(TimeUnit.MILLISECONDS.toSeconds(velop.toLong())).toInt()
                                }


                                val volPerc = Math.ceil(((volumecalcalculation.toDouble()) / 100.toDouble())*maxVol.toFloat())
                                volumeprogress.setProgress(volPerc.toInt())

                                 audio.setStreamVolume(AudioManager.STREAM_MUSIC,volPerc.toInt(),AudioManager.FLAG_PLAY_SOUND)
                                volmetext.setText(volumeprogress.progress.toString())
                                if(volumeprogress.progress<=0){
                                    volumeicon.setImageResource(R.drawable.ic_round_volume_off_24)
                                }
                                movedown=true



                            }else{

                                if(velop<1000&& volumecalcalculation<=100){
                                    var conversion=velop/1000
                                    if(conversion<0.4){
                                        volumecalcalculation=volumecalcalculation+0.5
                                    }else{
                                        var io=conversion*10
                                        volumecalcalculation=volumecalcalculation+io

                                    }
                                }
                                else if( volumecalcalculation<=100){
                                    volumecalcalculation=volumecalcalculation+(TimeUnit.MILLISECONDS.toSeconds(velop.toLong())).toInt()
                                }

                                val volPerc = Math.ceil(((volumecalcalculation.toDouble()) / 100.toDouble())*maxVol.toFloat())
                                volumeprogress.setProgress(volPerc.toInt())

                                audio.setStreamVolume(AudioManager.STREAM_MUSIC,volPerc.toInt(),AudioManager.FLAG_PLAY_SOUND)
                                volmetext.setText(volumeprogress.progress.toString())
                                volumeicon.setImageResource(R.drawable.hplib_volume)

                                    moveup=true
                            }




                            if(moveup && anotherdiffy<diffY){
                                baseY= event.getY()
                                baseX=event.getX()
                                moveup=false
                            }else if(movedown && anotherdiffy>diffY){
                                movedown=false
                                baseX=event.getX()
                                baseY=event.getY()
                            }
                            anotherdiffy=diffY.toDouble()



                        }



                    }else if (Math.abs(diffX) > Math.abs(diffY)&&brightness.visibility==View.GONE&&volume.visibility==View.GONE&&allow==true) {
                        if (Math.abs(diffX) > (MIN_DISTANCE)) {

                            seek.visibility = View.VISIBLE

                        var seekSpeed =
                            (TimeUnit.MILLISECONDS.toSeconds(simplexo.getDuration()) * 0.1)
                        var seekDur: String = ""
                        var velop =
                            Math.sqrt((mVelocityTracker!!.getXVelocity() * mVelocityTracker!!.getXVelocity() + mVelocityTracker!!.getYVelocity() * mVelocityTracker!!.getYVelocity()).toDouble())
                            calculatedTime = ((velop)).toInt()
                            if(diffX<0){
                                calculatedTime=-(calculatedTime)
                            }


                        var totime = String.format(
                            "%02d:%02d",
                            TimeUnit.MILLISECONDS.toMinutes(simplexo.getCurrentPosition() + (calculatedTime)) -
                                    TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(simplexo.getCurrentPosition() + (calculatedTime))), // The change is in this line
                            TimeUnit.MILLISECONDS.toSeconds(simplexo.getCurrentPosition() + (calculatedTime)) -
                                    TimeUnit.MINUTES.toSeconds(
                                        TimeUnit.MILLISECONDS.toMinutes(
                                            simplexo.getCurrentPosition() + (calculatedTime)
                                        )
                                    )
                        )
                        seekcurrtext.setText(totime)

                            var fuck=TimeUnit.MILLISECONDS.toMinutes((simplexo.currentPosition + calculatedTime)-curent)
                            var fuck1=(TimeUnit.MILLISECONDS.toSeconds((simplexo.currentPosition + calculatedTime)-curent) % 60)
                            Log.d("lal","oidh "+fuck+" "+fuck1)
                            if(fuck<0||fuck1<0){
                                if(Math.abs(fuck1)<10){
                                    seektext.setText("[ -0"+Math.abs(fuck).toString()+":0"+Math.abs(fuck1)+"]")
                                }else{
                                    seektext.setText("[ -0"+Math.abs(fuck).toString()+":"+Math.abs(fuck1)+"]")
                                }
                            }else{
                                if(fuck1<10){
                                seektext.setText("[ +0"+fuck+":0"+fuck1+"]")}
                                else{
                                    seektext.setText("[ +0"+fuck+":"+fuck1+"]")
                                }
                            }

                            simplexo.seekTo(simplexo.currentPosition + calculatedTime)


                    }
                        if(list.isEmpty()){
                            list.add(diffX.toFloat())
                            list.add(diffY.toFloat())
                            arrayList.addAll(list)
                            if(diffX>0){
                                moveright=true
                            }else{
                                moveleft=true
                            }
                        }else{
                            if(arrayList.get(0)<diffX&&moveleft==true){
                                baseX=event.getX()
                                moveleft=false
                            }
                            else if(arrayList.get(0)>diffX&&moveright==true){
                                baseX=event.getX()
                                moveright=false
                            }
                            list.clear()
                            arrayList.clear()

                        }

                    }
                    else{
                       if(tested_left){
                           brightness.visibility=View.VISIBLE
                           var velop =
                               Math.sqrt((mVelocityTracker!!.getXVelocity() * mVelocityTracker!!.getXVelocity() + mVelocityTracker!!.getYVelocity() * mVelocityTracker!!.getYVelocity()).toDouble())
                           if(diffY>0){
                               if(velop<1000 && brightcalculation>=0){
                                   var conversion=velop/1000
                                   if(conversion<0.4){
                                       brightcalculation=brightcalculation-0.4

                                       Log.d("consuming","down "+brightcalculation)
                                   }else{
                                       var io=conversion*10
                                       brightcalculation=brightcalculation-io

                                       Log.d("consuming","down1 "+brightcalculation+" "+io)

                                   }
                               }
                               else if(brightcalculation>=0){

                                   brightcalculation=brightcalculation-TimeUnit.MILLISECONDS.toSeconds(velop.toLong())
                               }

                               if(brightcalculation>=0) {
                                   movedown=true
                                   brightprogress.setProgress(((brightcalculation / 100) * 15).toInt())
                                   val bru = (brightcalculation.toFloat() / 100.0)
                                   val layoutpars: WindowManager.LayoutParams =
                                       window.getAttributes()
                                   layoutpars.screenBrightness = bru.toFloat()
                                   window.setAttributes(layoutpars)
                                   brighttext.setText(brightprogress.progress.toString())

                               }
                               else{
                                   movedown=true
                                   brightprogress.setProgress(0)
                                   brighttext.setText("0")
                               }
                           }else{
                               if(velop<1000 && brightcalculation<=100){
                                   var conversion=velop/1000
                                   if(conversion<0.4){
                                       brightcalculation=brightcalculation+0.4

                                       Log.d("consuming","up "+brightcalculation)
                                   }else{
                                       var io=conversion*10
                                       brightcalculation=brightcalculation+io

                                       Log.d("consuming","up1 "+brightcalculation+" "+io)

                                   }
                               }

                               else if(brightcalculation<=100){

                                   brightcalculation=brightcalculation+TimeUnit.MILLISECONDS.toSeconds(velop.toLong())
                               }
                               if(brightcalculation<=100) {
                                   moveup=true
                                   brightprogress.setProgress(((brightcalculation / 100) * 15).toInt())
                                   val bru = (brightcalculation.toFloat() / 100.0)
                                   val layoutpars: WindowManager.LayoutParams =
                                       window.getAttributes()
                                   layoutpars.screenBrightness = bru.toFloat()
                                   window.setAttributes(layoutpars)
                                   brighttext.setText(brightprogress.progress.toString())
                               }
                               else{
                                   moveup=true
                                   brightprogress.setProgress(15)
                                   brighttext.setText("15")
                               }

                           }
                           if(moveup && anotherdiffy<diffY){
                               baseY= event.getY()
                               baseX=event.getX()
                               moveup=false
                           }else if(movedown && anotherdiffy>diffY){
                               movedown=false
                               baseX=event.getX()
                               baseY=event.getY()
                           }
                           anotherdiffy=diffY.toDouble()

                       }else if(tested_right){
                           volume.visibility=View.VISIBLE
                           var maxVol=audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC)

                           var velop = Math.sqrt((mVelocityTracker!!.getXVelocity() * mVelocityTracker!!.getXVelocity() + mVelocityTracker!!.getYVelocity() * mVelocityTracker!!.getYVelocity()).toDouble())



                           if(diffY>0){
                               if(velop<1000 && volumecalcalculation>=0){
                                   var conversion=velop/1000
                                   if(conversion<0.4){
                                       volumecalcalculation=volumecalcalculation-0.5
                                   }else{
                                       var io=conversion*10
                                       volumecalcalculation=volumecalcalculation-io



                                   }
                               }

                               else if(volumecalcalculation>=0){
                                   volumecalcalculation=volumecalcalculation-(TimeUnit.MILLISECONDS.toSeconds(velop.toLong())).toInt()
                               }


                               val volPerc = Math.ceil(((volumecalcalculation.toDouble()) / 100.toDouble())*maxVol.toFloat())
                               volumeprogress.setProgress(volPerc.toInt())

                               audio.setStreamVolume(AudioManager.STREAM_MUSIC,volPerc.toInt(),AudioManager.FLAG_PLAY_SOUND)
                               volmetext.setText(volumeprogress.progress.toString())
                               if(volumeprogress.progress<=0){
                                   volumeicon.setImageResource(R.drawable.ic_round_volume_off_24)
                               }
                               movedown=true



                           }else{

                               if(velop<1000&& volumecalcalculation<=100){
                                   var conversion=velop/1000
                                   if(conversion<0.4){
                                       volumecalcalculation=volumecalcalculation+0.5
                                   }else{
                                       var io=conversion*10
                                       volumecalcalculation=volumecalcalculation+io

                                   }
                               }
                               else if( volumecalcalculation<=100){
                                   volumecalcalculation=volumecalcalculation+(TimeUnit.MILLISECONDS.toSeconds(velop.toLong())).toInt()
                               }

                               val volPerc = Math.ceil(((volumecalcalculation.toDouble()) / 100.toDouble())*maxVol.toFloat())
                               volumeprogress.setProgress(volPerc.toInt())

                               audio.setStreamVolume(AudioManager.STREAM_MUSIC,volPerc.toInt(),AudioManager.FLAG_PLAY_SOUND)
                               volmetext.setText(volumeprogress.progress.toString())
                               volumeicon.setImageResource(R.drawable.hplib_volume)

                               moveup=true
                           }




                           if(moveup && anotherdiffy<diffY){
                               baseY= event.getY()
                               baseX=event.getX()
                               moveup=false
                           }else if(movedown && anotherdiffy>diffY){
                               movedown=false
                               baseX=event.getX()
                               baseY=event.getY()
                           }
                           anotherdiffy=diffY.toDouble()
                       }
                    }

                }

            }
            MotionEvent.ACTION_UP->{
             anotherdiffy=0.0
                tested_right=false
                tested_left=false

                seek.visibility=View.GONE
                volume.visibility=View.GONE
                brightness.visibility=View.GONE
                if(playerview.isControllerVisible){
               playerview.showController()}
                else{
                    playerview.hideController()
                }

            }
            MotionEvent.ACTION_CANCEL -> {
                mVelocityTracker?.recycle()

                mVelocityTracker = null
            }
        }
        return true
    }




    override fun onDestroy() {
        super.onDestroy()
        if(comingintent==false){
            changing.changenew(ok.get(position.toInt()).title!!)
        }
        comingintent=false
        var lolliop=TimeUnit.MILLISECONDS.toSeconds(simplexo.currentPosition)
        GlobalScope.launch {

            var know=videoDatabase2.fileDao().updatedur(lolliop,databaseid,0)
        }



        simplexo.release()
    }




    private fun hideDefaultControls() {
        root_layo.visibility=View.INVISIBLE
        //Todo this function will hide status and navigation when we click on screen
        val window = this.window ?: return
        window.clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        val decorView = window.decorView
        if (decorView != null) {
            var uiOption = decorView.systemUiVisibility
            if (Build.VERSION.SDK_INT >= 14) {
                uiOption = uiOption or View.SYSTEM_UI_FLAG_LOW_PROFILE
            }
            if (Build.VERSION.SDK_INT >= 16) {
                uiOption = uiOption or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            }
            if (Build.VERSION.SDK_INT >= 19) {
                uiOption = uiOption or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            }
            decorView.systemUiVisibility = uiOption
        }
    }

    private fun showDefaultControls() {
        root_layo.visibility=View.VISIBLE
        playerview.showController()
        //todo this function will show status and navigation when we click on screen
        val window = this.window ?: return
        window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window.addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN)
        val decorView = window.decorView
        if (decorView != null) {
            var uiOption = decorView.systemUiVisibility
            if (Build.VERSION.SDK_INT >= 14) {
                uiOption = uiOption and View.SYSTEM_UI_FLAG_LOW_PROFILE.inv()
            }
            if (Build.VERSION.SDK_INT >= 16) {
                uiOption = uiOption and View.SYSTEM_UI_FLAG_HIDE_NAVIGATION.inv()
            }
            if (Build.VERSION.SDK_INT >= 19) {
                uiOption = uiOption and View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY.inv()
            }

            decorView.systemUiVisibility = uiOption


        }
    }
    val firstlistener: View.OnClickListener = object : View.OnClickListener {
        override fun onClick(v: View?) {
            playerview.resizeMode=AspectRatioFrameLayout.RESIZE_MODE_FILL
            simplexo.videoScalingMode=C.VIDEO_SCALING_MODE_DEFAULT
            scaling.setImageResource(R.drawable.fullscreen)
            scaling.setOnClickListener(secondlistener)
        }
    }
    val thirdlistener: View.OnClickListener = object : View.OnClickListener {
        override fun onClick(v: View?) {
            playerview.resizeMode=AspectRatioFrameLayout.RESIZE_MODE_FIT
            simplexo.videoScalingMode=C.VIDEO_SCALING_MODE_DEFAULT
            scaling.setImageResource(R.drawable.fit)
            scaling.setOnClickListener(firstlistener)


        }
    }
    val secondlistener: View.OnClickListener = object : View.OnClickListener {
        override fun onClick(v: View?) {
            playerview.resizeMode=AspectRatioFrameLayout.RESIZE_MODE_ZOOM
            simplexo.videoScalingMode=C.VIDEO_SCALING_MODE_DEFAULT
            scaling.setImageResource(R.drawable.zoom)
            scaling.setOnClickListener(thirdlistener)

        }
    }




    private fun playvideosubtitle(urisub:Uri) {
        var oldposition=simplexo.currentPosition
        simplexo.stop()

        simplexo=ExoPlayer.Builder(this,DefaultRenderersFactory(this)).build()
        val datasource=DefaultDataSourceFactory(this,Util.getUserAgent(this,"app"))
        concatenate=ConcatenatingMediaSource()
        for(i in ok.indices){

            File(ok.get(i).toString())
            val mediasource=ProgressiveMediaSource.Factory(datasource).createMediaSource(Uri.parse(ok.get(position.toInt()).path))

            val subtitle: MediaItem.SubtitleConfiguration = MediaItem.SubtitleConfiguration.Builder(urisub)
                .setMimeType(MimeTypes.APPLICATION_SUBRIP)
                .build()
            Log.d("menu","Sokso "+subtitle.language)
            var subtitlesource=SingleSampleMediaSource.Factory(datasource).setTreatLoadErrorsAsEndOfStream(true)
                .createMediaSource(subtitle,C.TIME_UNSET)
            var merge=MergingMediaSource(mediasource,subtitlesource)

            concatenate.addMediaSource(merge)


        }
        playerview.setPlayer(simplexo)
        simplexo.prepare(concatenate)

        simplexo.seekTo(position.toInt(),oldposition)
        playError()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_more,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==R.id.addsub){
            dialogproperties.selection_mode=DialogConfigs.SINGLE_MODE
            dialogproperties.extensions = arrayOf(".srt")
            dialogproperties.root= File("/storage/emulated/0")
            filePickerDialog.properties=dialogproperties
            filePickerDialog.show()
            filePickerDialog.setDialogSelectionListener(object : DialogSelectionListener{
                override fun onSelectedFilePaths(files: Array<out String>?) {
                    files!!.forEach { f->
                        var file=File(f)
                        uriSubtitle= Uri.parse(file.absolutePath.toString())
                    }
                    playvideosubtitle(uriSubtitle)
                }

            })

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        val x = event!!.values[0]
        val y = event.values[1]
        var newOrientation: Int = -1
        if (x < 5 && x > -5 && y > 5) {
            newOrientation = 0

        }
        else if (x < -5 && y < 5 && y > -5){
            newOrientation = 90
        }
        else if (x < 5 && x > -5 && y < -5) {
            newOrientation = 180
        }
        else if (x > 5 && y < 5 && y > -5){
            newOrientation = 270
        }
        val orientation = resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE && newOrientation==90) {
            //set in landscape
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE}
        else if(orientation == Configuration.ORIENTATION_LANDSCAPE && newOrientation==270){
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }
        if(playerview.isControllerVisible==false){
            hideDefaultControls()
        }
        Log.d("lalit","popi "+newOrientation)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }
    override fun onResume() {
        super.onResume()
        sensorManage.registerListener(this,acclerometer,SensorManager.SENSOR_DELAY_NORMAL)
        simplexo.playWhenReady=true
        simplexo.playbackState

    }

    override fun onPause() {
        super.onPause()
        sensorManage.unregisterListener(this)
        simplexo.playWhenReady=false
        simplexo.playbackState
    }

    override fun onRestart() {
        super.onRestart()
        simplexo.playWhenReady=true
        simplexo.playbackState
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if(simplexo.isPlaying){
            simplexo.stop()
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        var maxVol=audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC)

        when(event!!.keyCode){

            KeyEvent.KEYCODE_VOLUME_UP->{
                event.startTracking()
                audio.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                    AudioManager.ADJUST_RAISE,
                    AudioManager.FLAG_PLAY_SOUND)

                volume.visibility=View.VISIBLE


                volumeprogress.setProgress((audio.getStreamVolume(AudioManager.STREAM_MUSIC)))

                volmetext.setText(volumeprogress.progress.toString())
                volumeicon.setImageResource(R.drawable.hplib_volume)

                return true
            }
            KeyEvent.KEYCODE_VOLUME_DOWN->{
                event.startTracking()

                audio.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                    AudioManager.ADJUST_LOWER,
                    AudioManager.FLAG_PLAY_SOUND)

                volume.visibility=View.VISIBLE



                volumeprogress.setProgress(audio.getStreamVolume(AudioManager.STREAM_MUSIC))

                volmetext.setText(volumeprogress.progress.toString())

                if(volumeprogress.progress<=0){
                    volumeicon.setImageResource(R.drawable.ic_round_volume_off_24)
                }



                return true

            }


        }


        return super.onKeyDown(keyCode, event)
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {


      volume.visibility=View.INVISIBLE
        return super.onKeyUp(keyCode, event)
    }




}












