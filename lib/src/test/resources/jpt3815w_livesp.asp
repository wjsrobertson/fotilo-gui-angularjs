<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<link rel="stylesheet" href="/css/live_blk.css" type="text/css"/>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<script type="text/javascript" src="/lang/b28n.js"></script>
<script type="text/javascript" src="/tool/sbar.js"></script>
<script language="JavaScript" type="text/javascript">

Butterlate.setTextDomain("video");

var page_acc=3
var ptz_enb=1;
var preset_enb=128;
var ptz_start=0;
var ptz_speed=0;
var ptz_speed_max=12;
var ptz_preset_max=6;
var speaker_enb=1;
var voice_enb=1;
var ioport_enb=0;

var preset_act = 0;
var disp_obj = null;

var show_item = "";

function lay_sel(obj, cnt)
{
    show_cams(cnt);

    obj.style.borderStyle ="inset";
    if ( sel_lay != null )
    {
        sel_lay.style.borderStyle ="none";
    }
    sel_lay = obj;
}


function page_init() {
    if (window.ActiveXObject)
    {
        window.location = "liveie.asp";
        return ;
    }

    initTranslation();

var cmbspeed = document.getElementById("sel_ptz_pseed");
    var itm = new Option(_("Fastest"),0);
    cmbspeed.options.add(itm);
    itm = new Option(_("Faster"),2);
    cmbspeed.options.add(itm);
    itm = new Option(_("Normal"),4);
    cmbspeed.options.add(itm);
    itm = new Option(_("Slower"),7);
    cmbspeed.options.add(itm);
    itm = new Option(_("Slowest"),10);
    cmbspeed.options.add(itm);
    cmbspeed.value = ptz_speed;

     sbar_pos('pos_brig', 4, 'cam_brightness');
sbar_pos('pos_cntr', 2, 'cam_contrast');

    document.getElementById("img_live").src="/vjpeg.v";

    cmd_frm.init_comp();

    if ( page_acc < 2 )
    {
    	document.getElementById("lay_set").style.display="none";
    }
    if ( page_acc < 1 )
    {
        document.getElementById("tool_vdsize").innerHTML = "&nbsp;";
        document.getElementById("tool_fps").innerHTML = "&nbsp;";
        document.getElementById("tool_ptz").innerHTML = "&nbsp;";
        document.getElementById("tool_ptzrun").innerHTML = "&nbsp;";
        document.getElementById("tool_img").innerHTML = "&nbsp;";

        document.getElementById("tool_vdsize").innerHTML = "&nbsp;";
        document.getElementById("tool_fps").innerHTML = "&nbsp;";
        document.getElementById("tool_vdsize").style.height="0px";
        document.getElementById("tool_fps").style.height="0px";
        document.getElementById("tool_ptz").innerHTML = "&nbsp;";
        document.getElementById("tool_ptzrun").innerHTML = "&nbsp;";
        document.getElementById("tool_img").innerHTML = "&nbsp;";
        document.getElementById("tool_ptzspeed").innerHTML = "&nbsp;";
        document.getElementById("slpt_ptz").style.display="";
        document.getElementById("slpt_ptz").style.height="200px";

        return;
    }

    document.getElementById('img_live').style.width='640px';document.getElementById('img_live').style.height='480px';
    switch(32)
    {
    		case 32:
	    		document.getElementById("selStream").value = 41943520;
	    		break;
    		case 8:
	    		document.getElementById("selStream").value = 20971760;
	    		break;
    		case 2:
	    		document.getElementById("selStream").value = 10485880;
	    		break;
    	}

    			document.getElementById("camFPS").value = 30

		if ( ptz_enb == 0 )
		{
				document.getElementById("tool_ptz").style.display="none";
				document.getElementById("tool_ptzrun").style.display="none";
                document.getElementById("slpt_ptz").style.height="220px";

		}
		if ( speaker_enb == 0 )
		{

		}
}

function show_preset_table(e)
{
    if ( preset_enb == 0 ) return ;

    e = e ? e : window.event;
     if ( disp_obj != null )
    {
        disp_obj.style.display ="none";
        disp_obj = null;
    }
    /*
	if ( document.getElementById("tblPtzSave").style.display == "block" )
	{
	    document.getElementById("tblPtzSave").style.display ="none";
	}
	else*/
	{
	    document.getElementById("tblPtzSave").style.display ="block"
	    document.getElementById("tblPtzSave").style.left=(e.clientX - 80) + "px";
	    document.getElementById("tblPtzSave").style.top=(e.clientY + 10)+ "px";
        disp_obj = document.getElementById("tblPtzSave");
	}
}

function preset_doact(idx) {
    document.getElementById("tblPtzSave").style.display = "none";
    if ( preset_act == 1 )
    {
        cmd_frm.ptz_savepos(idx);
        //setTimeout(window.location.reload, 400);
    }
    else
    {
        cmd_frm.ptz_gotopreset(idx);
    }
}

function show_obj(e)
{
    e = e ? e : window.event;
    var obj = document.getElementById(show_item);

    if ( obj == null ) return ;

    if ( disp_obj != null )
    {
        disp_obj.style.display ="none";
        disp_obj = null;
    }

    if ( obj.style.display != "block" )
    {
	    obj.style.display ="block"
	    obj.style.left=(e.clientX - 160) + "px";
	    obj.style.top=(e.clientY + 10)+ "px";
        disp_obj = obj ;
    }
    else
    {
        obj.style.display ="none";
    }
}

function close_item(itnm)
{
    var obj = document.getElementById(itnm);
    if ( obj != null )
        obj.style.display ="none";
}

function refresh_stream()
{
	location.reload();
}
function img_size(vlu)
{
	cmd_frm.imagesize_cmd(vlu);
	setTimeout('refresh_stream()', 1000);
}
function save_preset_do(e) {
        preset_act = 1;
        show_preset_table(e);
    }
function call_preset_evt(e) {
    preset_act=0;
    show_preset_table(e);
}

function brt_set_evt(e) {
    show_item = 'sbar_brt';
    show_obj(e);
}
function cnst_set_evt(e) {
    show_item = 'sbar_cnstr';
    show_obj(e);
}

var p_loop_ud = 0;
function ptz_loop_ud()
{
	if ( p_loop_ud == 0 )
    {
		p_loop_ud = 1;
        setTimeout('ptz_loop_end(1)', 1000 * 18);
    }
	else
		p_loop_ud = 0;

	cmd_frm.pzt_loop_ud(p_loop_ud);
}

var p_loop_lr = 0;
function ptz_loop_lr()
{
	if ( p_loop_lr == 0 )
    {
		p_loop_lr = 1;
        setTimeout('ptz_loop_end(2)', 1000 * 30);
    }
	else
		p_loop_lr = 0;

	cmd_frm.pzt_loop_lr(p_loop_lr);
}

var p_loop_bth = 0;
function ptz_loop_both()
{
	/*
	if ( p_loop_bth == 0 )
    {
		p_loop_bth = 1;
        setTimeout('ptz_loop_end(3)', 1000 * 30);
    }
	else
    {
		p_loop_bth = 0;
        p_loop_lr = 0;
        p_loop_ud = 0;
	}

	cmd_frm.pzt_loop_both(p_loop_bth);
	*/
	cmd_frm.pzt_loop_both(1);
}

function ptz_loop_end(lp_pos)
{
    if (lp_pos == 1 )
        p_loop_ud = 0;
    else if ( lp_pos == 2 )
        p_loop_lr = 0;
    else if ( lp_pos == 3 )
    {
		p_loop_bth = 0;
        p_loop_lr = 0;
        p_loop_ud = 0;
    }
}

function initTranslation()
{
	var e;
    /*
    e = document.getElementById("tt_video");
	e.innerHTML = _("video_infor") + "&hellip;" + "<a href=\"/IPCPlayerPlug.exe\">[" +_("video_player") +"]</a>";
    */
	e = document.getElementById("vd_size");
	e.innerHTML = _("video_size");
	e = document.getElementById("vd_fps");
	e.innerHTML = _("video_fps");
    e = document.getElementById("speed_ptz");
	e.innerHTML = _("speed_ptz");

	e = document.getElementById("ptzUp");
	e.title = _("ptz_up");
    e.alt = e.title;
	e = document.getElementById("ptzLeft");
	e.title = _("ptz_left");
    e.alt = e.title;
	e = document.getElementById("ptzRight");
	e.title = _("ptz_right");
    e.alt = e.title;
	e = document.getElementById("ptzDown");
	e.title = _("ptz_down");
    e.alt = e.title;
    e = document.getElementById("ptzSave");
	e.title = _("ptz_save");
    e.alt = e.title;
	e = document.getElementById("rd_h");
	e.title = _("horizontal_patrol");
    e.alt = e.title;
	e = document.getElementById("rd_v");
	e.title = _("verticality_patrol");
    e.alt=e.title;
	e = document.getElementById("rd_all");
	e.title = _("rotate");
    e.alt = e.title;
	e = document.getElementById("call_preset");
	e.title = _("call preset");
    e.alt=e.title;

	e = document.getElementById("vd_vf");
	e.title = _("video_vflip");
    e.alt = e.title;
	e = document.getElementById("vd_hf");
	e.title = _("video_hflip");
    e.alt=e.title;
	e = document.getElementById("brt");
	e.title = _("brightness");
    e.alt = e.title;
	e = document.getElementById("cnst");
	e.title = _("video_contrast");
    e.alt=e.title;

	e = document.getElementById("lay_set");
	e.title = _("settings");
    e.alt = e.title;
	e = document.getElementById("btnSnapshot");
	e.title = _("snapshot");
    e.alt=e.title;

    e = document.getElementById("save_pos");
    e.innerHTML=_("preset");
    e = document.getElementById("vd_cts");
    e.innerHTML=_("video_contrast") + "&nbsp;&nbsp;" + _("default");
    e = document.getElementById("vd_brt");
    e.innerHTML=_("brightness")+ "&nbsp;&nbsp;" + _("default");
}

</script>

<title>Live Video</title>
</head>
<!--  -->
<body onload="page_init()" oncontextmenu="return false;">

    <div class="ipd" id="ipd">
        <div class="ipd_left">
            <div class="ipd_left_top"></div>
            <div class="ipd_home">
                <a href="/index.asp">
                    <img src="/img/null_img.gif" class="ipd_home_btn" alt="Return" title="Return" />
                </a>
            </div>
        </div>
        <div class="ipd_rt">
            <div class="ipd_rt_lf">
            </div>
            <div class="ipd_rt_rt">
            </div>
            <div class="ipd_rt_md">
                <div class="ipd_logo"></div>

                <div class="ipd_rwtool">
                <!--
                    <span class="ipd_tool"><a href="#"><img id="lay1" title="Split_1" alt="Split_1" src="/img/tl_win1.gif" onclick="lay_sel(this, 1);"/></a></span>
                    <span class="ipd_tool"><a href="#"><img id="lay4" title="Split_4" alt="Split_4" src="/img/tl_win4.gif" onclick="lay_sel(this, 4);"/></a></span>
                    <span class="ipd_tool"><a href="#"><img id="lay9" title="Split_9" alt="Split_9"src="/img/tl_win9.gif" onclick="lay_sel(this, 9);"/></a></span>
                    <span class="ipd_tool"><a href="#"><img id="lay_set" title="Settings" alt="Settings" src="/img/tl_set.gif" onclick="window.location ='/setting.asp';"/></a></span>
                    -->
                </div>
                <div style="height:12px;"></div>
                <div class="ipd_rwinp" id="tool_vdsize" >
                    <span id="vd_size" class="ipd_lab">Resolution:</span>
                    <select id="selStream" class="ipd_input"  onchange="img_size(this.value);">
                               <option value="41943520">640 x 480</option>
                               <option value="20971760">320 x 240</option>
                               <option value="10485880">160 x 120</option>
                    </select>
                </div>
                <div class="ipd_rwinp" id="tool_fps">
                    <span id="vd_fps" class="ipd_lab">FPS:</span>
                    <select class="ipd_input" name="fps" id="camFPS" onChange="cmd_frm.fps_cmd(this.value);">
							 		 <option value="30" >30</option>
		 									<!--	<option value="25" >25</option>			-->
											 <option value="20" >20</option>
											 <option value="15" >15</option>
											 <option value="10" >10</option>
											 <option value="5" >5</option>
				    </select>
                </div>
                <div class="ipd_rwinp" id="tool_ptzspeed">
                    <span id="speed_ptz" class="ipd_lab">PTZ Speed:</span>
                     <select id="sel_ptz_pseed" name="ptz_speed" class="ipd_input" onchange="cmd_frm.pzt_speed(this.value);">
                     </select>
                </div>
                <div style="height:24px;"></div>
                <div class="ipd_rwptz" id="tool_ptz">
                    <img alt="PTZ Control" src="/img/button_nav.gif" usemap="#ptz_map" />
		              <map id="ptz_map_def" name="ptz_map">
                        <area id="ptzLeftUp" onmousedown="cmd_frm.pzt_upleft(1);p_loop_ud=0;p_loop_lr=0;" onmouseup="cmd_frm.pzt_upleft(0)" href="#" shape="rect" coords="0,0,35,35"/>
                 		<area id="ptzUp" alt="Up" title="Up" onmousedown="cmd_frm.pzt_up(1);p_loop_ud=0;p_loop_lr=0;" onmouseup="cmd_frm.pzt_up(0)" href="#" shape="rect" coords="43,0,78,35"/>
                        <area id="ptz_RightUp"onmousedown="cmd_frm.pzt_upright(1);p_loop_ud=0;p_loop_lr=0;" onmouseup="cmd_frm.pzt_upright(0)" href="#" shape="rect" coords="86,0,120,35"/>
                        <area id="ptzLeft" alt="Left" title="Left" onmousedown="cmd_frm.pzt_right(1);p_loop_ud=0;p_loop_lr=0;" onmouseup="cmd_frm.pzt_right(0)" href="#" shape="rect" coords="0,43,35,78"/>
                        <area id="rd_all"  title="rotate" alt="rotate" onclick="ptz_loop_both();" href="#" shape="rect" coords="43,43,78,78" />
                        <area id="ptzRight" alt="Right" title="Right" onmousedown="cmd_frm.pzt_left(1);p_loop_ud=0;p_loop_lr=0;" onmouseup="cmd_frm.pzt_left(0)" href="#" shape="rect" coords="86,43,120,78"/>
                        <area id="ptzLeftDown" onmousedown="cmd_frm.pzt_downleft(1);p_loop_ud=0;p_loop_lr=0;" onmouseup="cmd_frm.pzt_downleft(0)" href="#" shape="rect" coords="0,86,35,120"/>
                 		<area id="ptzDown"  alt="Down" title="Down" onmousedown="cmd_frm.pzt_down(1);p_loop_ud=0;p_loop_lr=0;" onmouseup="cmd_frm.pzt_down(0)" href="#" shape="rect" coords="43,86,78,120"/>
                        <area id="ptzRightDown" onmousedown="cmd_frm.pzt_downright(1);p_loop_ud=0;p_loop_lr=0;" onmouseup="cmd_frm.pzt_downright(0)" href="#" shape="rect" coords="86,86,120,120"/>
                      </map>
                </div>
                <div style="height:12px;" id="slpt_ptz"></div>
                <div class="ipd_rwbtn" id="tool_ptzrun">
                    <span class="ipd_button"><a href="#"><img id="rd_h" title="horizontal_patrol" alt="horizontal_patrol" src="/img/btn_mov_lr.gif" onclick="ptz_loop_lr();" /></a> </span>
                    <span class="ipd_button"><a href="#"><img id="rd_v" title="verticality_patrol" alt="verticality_patrol" src="/img/btn_mov_ud.gif" onclick="ptz_loop_ud();" /></a> </span>
                    <span class="ipd_button"><a href="#"><img id="ptzSave"  alt="Save Current to preset" title="Save Current to preset" onclick="{preset_act=1;show_preset_table();}" src="/img/btn_save.gif" /></a> </span>
                    <span class="ipd_button"><a href="#"><img id="call_preset" title="call preset" alt="call preset" src="/img/btn_go.gif"/></a> </span>
                </div>
                <div class="ipd_rwbtn" id="tool_img">
                    <span class="ipd_button"><a href="#"><img id="vd_hf" title="flip_horizontal" alt="flip_horizontal" src="/img/btn_flp_lr.gif" onclick="cmd_frm.img_fh(13);"></a> </span>
                    <span class="ipd_button"><a href="#"><img id="vd_vf" title="flip_verticality" alt="flip_verticality" src="/img/btn_flp_ud.gif" onclick="cmd_frm.img_fv(23);"></a> </span>
                    <span class="ipd_button"><a href="#"><img id="brt" title="brightness" alt="brightness" src="/img/btn_brt.gif"></a> </span>
                    <span class="ipd_button"><a href="#"><img id="cnst" title="contrast " alt="contrast " src="/img/btn_cst.gif"></a> </span>
                </div>
                 <div class="ipd_fun_spl">
                    <span class="ipd_spl_lab"></span>
                 </div>
                <div class="ipd_rwfun">
                    <center>
              <!--      <span class="ipd_tool"><a href="/media/?action=snapshot" target="_blank"><img id="btnSnapshot" title="snapshot" alt="snapshot" src="/img/btn_snp.gif"></a> </span>	-->
               			<span class="ipd_tool"><a href="/cgi-bin/snapshot.cgi" target="_blank"><img id="btnSnapshot" title="snapshot" alt="snapshot" src="/img/btn_snp.gif"></a> </span>
                    <span class="ipd_tool"><img alt=""src="/img/null_img.gif" /> </span>
                    <span class="ipd_tool"><a href="#"><img id="lay_set" width="30px" height="30px" title="Settings" alt="Settings" src="/img/tl_set.gif" onclick="window.location ='/setting.asp';"/></a></span>
                    </center>
                </div>

            </div>
        </div>
        <div class="ipd_md">
            <div class="ipd_cont" id="ipd_cont">
                <table border="0" cellpadding="0" cellspacing="0" width="640" height="480">
                <tr>
                    <td valign="middle" align="center" style="background-color:Black">
                        <img id="img_live" src="/img/wht.jpg" alt="" width="640" height="480"/>
                    </td>
                </tr>
                </table>

            </div>
        </div>
    </div>

<div id="tblPtzSave" class="sel_box">
    <div class="sel_box_bar">
        <div class="sel_title"  id="save_pos" >Preset</div>
        <span class="sel_bar_x"><a class="sel_bar_lnk" href="#" onclick="close_item('tblPtzSave');">X</a></span>
    </div>

    <span class="sel_item" onclick="preset_doact(1);"><a href="#">1</a></span>
    <span class="sel_item" onclick="preset_doact(2);" ><a href="#">2</a></span>
    <span class="sel_item" onclick="preset_doact(3);" ><a href="#">3</a></span>
    <span class="sel_item" onclick="preset_doact(4);" ><a href="#">4</a></span>
    <span class="sel_item" onclick="preset_doact(5);" ><a href="#">5</a></span>
    <span class="sel_item" onclick="preset_doact(6);" ><a href="#">6</a></span>
</div>
<div  class="sel_sbar" id="sbar_cnstr">
<div class ="sel_bar">
    <div class="sel_bar_title"><a class="sel_bar_lnk" href="#" onclick="sbar_pos('pos_cntr', 3, 'cam_contrast');cmd_frm.contrast_cmd(3);" id="vd_cts">Contrast:</a></div>
    <input readonly="readonly" id="cam_contrast" name="cam_contrast" value="0" onchange="cmd_frm.contrast_cmd(this.value);"/>
    <span class="sel_bar_x"><a class="sel_bar_lnk" href="#"onclick="close_item('sbar_cnstr');">X</a></span>
</div>
<div class='range_bar'>
	<div class='mea' id="pos_cntr" name="pos_cntr" onmousedown="sbar_change(this,event, 'cam_contrast',cmd_frm.contrast_cmd)" ></div>
</div>
</div>

<div class="sel_sbar" id="sbar_brt">
<div class ="sel_bar">
    <div class="sel_bar_title"><a class="sel_bar_lnk" href="#" onclick="sbar_pos('pos_brig', 4, 'cam_brightness');cmd_frm.brightness_cmd(4);" id="vd_brt">Brightness:</a></div>
    <input readonly="readonly" id="cam_brightness" name="cam_brightness" value="0" onchange="cmd_frm.brightness_cmd(this.value);"/>
    <span class="sel_bar_x"><a class="sel_bar_lnk" href="#"onclick="close_item('sbar_brt');">X</a></span>
</div>
<div class='range_bar'>
	<div class='mea' id="pos_brig" onmousedown="sbar_change(this,event, 'cam_brightness', cmd_frm.brightness_cmd)" ></div>
</div>
</div>

<div id="cam_hid" style="visibility: hidden">
</div>
<script language="JavaScript" type="text/javascript">
    document.getElementById("call_preset").onclick = call_preset_evt;
    document.getElementById("ptzSave").onclick = save_preset_do;
    document.getElementById("brt").onclick = brt_set_evt;
    document.getElementById("cnst").onclick = cnst_set_evt;
</script>

<iframe id="cmd_frm" name="cmd_frm" src="cmd_req.asp" width="0" height="0"
        marginwidth="0" marginheight="0"  scrolling="no" style="visibility: hidden"></frame>

</body>

</html>