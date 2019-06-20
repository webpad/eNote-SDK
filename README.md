# eNote-SDK

### Programing Guide for MobiScribe eNote
Version 2.0
2019/03/26
Rev. 1.3

## Table of Contents
INTRODUCTION 

Development REQUIREMENTS 

Operate System

Java Environment

Android Environment

IMPLEMENTATION 

MobiScribe Library (SDK)

E-ink Refresh Library

Example Code for Full Refresh Display

Handwriting Library

Example Code for Initializing nDraw

Example Code for Setting Draw Region

Example Code for Setting Multi Draw Region 

Example Code for switch Hand Writing mode and pen offset 

Example Code for Setting Pen Type 

Example Code for Setting nDraw Paint Stroke Width

Example Code for Setting Pen Thickness with Pressure of Writing 

Example Code for Setting nDraw Paint Color 

Example Code for Changing Update Mode of Writing

Example Code for Setting Orientation of Writing Area 

Example Code for Taking Screenshots of Writing Area and Entire Screen

Example Code for Setting Time Duration of Dropping Frames

Appendix 

Refresh Control Broadcast Receiver 

2-Step-Suspend

adb Setup

USB Driver Installation for Windows 7 

Download Android SDK platform-tools

Add Android Vendor ID

Execute adb server 

Eink Features/Waveforms

Standard(4 bits) Waveform Modes 

Regal (5 bits) Waveform Modes 



## INTRODUCTION 

This document is provided by MobiScribe SDK Library, the purpose of
providing software design guide. The SDK Library uses the JAVA language
design, which contains two classes.jar and javalib.jar JAVA Library.

## Development REQUIREMENTS

# Operate System:

- Windows 7 64-bit above version. 􀀀
- Mac OS X 􀀀
- Ubuntu 14.05 64-bit version 􀀀
# Java Environment:
- Java Runtime Environment (JRE 1.8.0)
- Java Development Kit (JDK 8)
# Android Environment:
- Android SDK 4.4 (API level 19).
- Android Studio 2.1.3 above version.

4
Rev. 1.3
Programing Guide for MobiScribe eNote
IMPLEMENTATION
MobiScribe Library (SDK):
There are 2 main libraries for E-ink Refresh and Handwriting , customer
can include these libraries to make the performance better
5
Rev. 1.3
Programing Guide for MobiScribe eNote
E-ink Refresh Library
Import view class :
import android.view.View ;
Summary
Return Parameter
void
invalidate( Rect region , int updateMode );
Rect region : set invalidate area
int updateMode : EINK update mode
void invalidate( int updateMode );
int updateMode : EINK update mode
Example Code for Full Refresh Display:
invalidate( UPDATE_MODE_FULL_GC16 );
EINK update mode (1/3)
import android.view.View ;
public static final int UPDATE_MODE_FULL_GC16 =
View. EINK_WAVEFORM_MODE_GC16
| View. EINK_UPDATE_MODE_FULL ;
public static final int UPDATE_MODE_PARTIAL_DU =
View. EINK_WAVEFORM_MODE_DU
| View. EINK_UPDATE_MODE_PARTIAL ;
6
Rev. 1.3
Programing Guide for MobiScribe eNote
EINK Mode (2/3)
public static final int UPDATE_MODE_PARTIAL_A2 =
View. EINK_WAVEFORM_MODE_A2
| View. EINK_UPDATE_MODE_PARTIAL ;
public static final int UPDATE_MODE_PARTIAL_GC4 =
View. EINK_WAVEFORM_MODE_GC4
| View. EINK_UPDATE_MODE_PARTIAL ;
public static final int UPDATE_MODE_PARTIAL_DU_WITH_DITHER =
View. EINK_WAVEFORM_MODE_DU
| View. EINK_UPDATE_MODE_PARTIAL
| View. EINK_DITHER_MODE_DITHER ;
public static final int UPDATE_MODE_PARTIAL_A2_WITH_DITHER =
View. EINK_WAVEFORM_MODE_A2
| View. EINK_UPDATE_MODE_PARTIAL
| View. EINK_DITHER_MODE_DITHER ;
public static final int UPDATE_MODE_PARTIAL_GC4_WITH_DITHER =
View. EINK_WAVEFORM_MODE_GC4
| View. EINK_UPDATE_MODE_PARTIAL
| View. EINK_DITHER_MODE_DITHER ;
public static final int UPDATE_MODE_PARTIAL_DU_WITH_MONO =
View. EINK_WAVEFORM_MODE_DU
| View. EINK_UPDATE_MODE_PARTIAL
| View. EINK_DITHER_MODE_DITHER
| View. EINK_MONOCHROME_MODE_MONOCHROME ;
7
Rev. 1.3
Programing Guide for MobiScribe eNote
EINK Mode (3/3)
public static final int UPDATE_MODE_PARTIAL_A2_WITH_MONO =
View. EINK_WAVEFORM_MODE_A2
| View. EINK_UPDATE_MODE_PARTIAL
| View. EINK_DITHER_MODE_DITHER
| View. EINK_MONOCHROME_MODE_MONOCHROME ;
public static final int UPDATE_MODE_PARTIAL_GC4_WITH_MONO =
View. EINK_WAVEFORM_MODE_GC4
| View. EINK_UPDATE_MODE_PARTIAL
| View. EINK_DITHER_MODE_DITHER
| View. EINK_MONOCHROME_MODE_MONOCHROME ;
public static final int UPDATE_MODE_PARTIAL_GC16 =
View. EINK_WAVEFORM_MODE_GC16
| View. EINK_UPDATE_MODE_PARTIAL ;
public static final int UPDATE_MODE_PARTIAL_AUTO =
View. EINK_WAVEFORM_MODE_AUTO
| View. EINK_UPDATE_MODE_PARTIAL ;
public static final int UPDATE_MODE_FULL_A2 =
View. EINK_WAVEFORM_MODE_A2
| View. EINK_UPDATE_MODE_FULL
| View. EINK_MONOCHROME_MODE_MONOCHROME ;
public static final int UPDATE_MODE_FULL_DU =
View. EINK_WAVEFORM_MODE_DU
| View. EINK_UPDATE_MODE_FULL
| View. EINK_MONOCHROME_MODE_MONOCHROME ;
public static final int MODE_APPNDRAWSTROKESYNC = 0x01000000;
8
Rev. 1.3
Programing Guide for MobiScribe eNote
Handwriting Library
Import nDrawHelper class :
import ntx.draw.nDrawHelper;
Summary
Return Parameter
void NDrawInit();
no parameter; initialize nDraw
void NDrawSetDrawRegion(int[] packet);
int [] packet { left, top, right, bottom} : set draw region
void
NDrawSwitch( boolean b )
boolean b = true : nDrawHelper enable
boolean b = false : nDrawHelper disable
void
NDrawSetInputOffset( int pen_offset_x , int pen_offset_y )
int pen_offset_x : set pen offset x-axis coordinate
int pen_offset_y : set pen offset y-axis coordinate
void NDrawSetPenType( int penType)
int penType: set pen type
void NDrawSetStrokeWidth( int lineWidth )
int lineWidth : set line width or pen thickness
void NDrawSetPaintColor( int color )
int color : set paint color
void NDrawSetUpdateMode(int refresh_mode)
int refresh_mode: set update mode of writing
void NDrawSetMaxStrokeWidthWhenUsingPressure(int max_pressure )
int max_pressure: set maximum pen thickness when using pressure
void NDrawSaveSignature()
no parameter; take screenshots of writing area and entire screen
void NDrawSetInputRotation(int mCurrentRotation );
int mCurrentRotation: set orientation of writing
void NDrawDropFrames(long nanosecond )
long nanosecond : set time of don't update screen
9
Rev. 1.3
Programing Guide for MobiScribe eNote
Example Code for Initializing nDraw:
@Override
protected void onCreate(Bundle savedInstanceState) {
super .onCreate(savedInstanceState);
nDrawHelper. NDrawInit ();
}
Return Parameter
void NDrawInit();
no parameter; initialize nDraw
10
Rev. 1.3
Programing Guide for MobiScribe eNote
Example Code for Setting Draw Region
int Ndraw_region_left = 0 ;
int Ndraw_region_top = getRelativeTop(NtxView. this );
int Ndraw_region_right = Ndraw_region_left + w;
int Ndraw_region_bottom = Ndraw_region_top + h;
int [] packet1 = {Ndraw_region_left, Ndraw_region_top, Ndraw_region_right,
Ndraw_region_bottom};
nDrawHelper.NDrawSetDrawRegion(packet1);
private int getRelativeTop(View myView ) {
if ( myView .getParent() == myView .getRootView())
return myView .getTop();
else
return myView .getTop() + getRelativeTop((View) myView .getParent());
}
Return Parameter
void
NDrawSetDrawRegion(int[] packet);
int [] packet { left, top, right, bottom} : set draw region
11
Rev. 1.3
Programing Guide for MobiScribe eNote
Example Code for Setting Multi Draw Region
int Ndraw_region_left = 0 ;
int Ndraw_region_top = getRelativeTop(NtxView. this );
int Ndraw_region_right = Ndraw_region_left + w/ 3 ;
int Ndraw_region_bottom = Ndraw_region_top + h/ 3 ;
int Ndraw_region_left_2 = (w * 2 )/ 3 ;
int Ndraw_region_top_2 = getRelativeTop(NtxView. this );
int Ndraw_region_right_2 = w;
int Ndraw_region_bottom_2 = Ndraw_region_top_2 + h/ 3 ;
int Ndraw_region_left_3 = w/ 3 ;
int Ndraw_region_top_3 = getRelativeTop(NtxView. this ) + h/ 3 ;
int Ndraw_region_right_3 = Ndraw_region_left_3 + w/ 3 ;
int Ndraw_region_bottom_3 = Ndraw_region_top_3 + h/ 3 ;
int [] multi_active_region = {Ndraw_region_left, Ndraw_region_top, Ndraw_region_right,
Ndraw_region_bottom,
Ndraw_region_left_2, Ndraw_region_top_2, Ndraw_region_right_2,
Ndraw_region_bottom_2,
Ndraw_region_left_3, Ndraw_region_top_3, Ndraw_region_right_3,
Ndraw_region_bottom_3};
nDrawHelper. NDrawSetDrawRegion (multi_active_region);
private int getRelativeTop(View myView ) {
if ( myView .getParent() == myView .getRootView())
return myView .getTop();
else
return myView .getTop() + getRelativeTop((View) myView .getParent());
}
Return Parameter
void
NDrawSetDrawRegion(int[] packet);
int [] packet { left1, top1, right1, bottom1, left2, top2, right2, bottom2, left3,
top3, right3, bottom3} : set multi draw region
12
Rev. 1.3
Programing Guide for MobiScribe eNote
Example Code for switch Hand Writing mode and pen offset :
// Set nDraw enable
nDrawHelper.NDrawSwitch( true );
// Set the pen offset, nDraw and Canvas offset must be the same.
public void setInputOffset(int offset_x, int offset_y){
nDrawHelper.NDrawSetInputOffset(offset_x, offset_y);
intCanvasOffsetX=offset_x;
intCanvasOffsetY=offset_y;
}
// Sync the pen stroke of the canvas to the framebuffer instantly when pen up
int mode = MODE_APPNDRAWSTROKESYNC | UPDATE_MODE_FULL_GC16 ;
invalidate(mode);
Return Parameter
void
NDrawSwitch( boolean b )
boolean b = true : nDrawHelper enable
boolean b = false : nDrawHelper disable
void
NDrawSetInputOffset( int pen_offset_x , int pen_offset_y )
int pen_offset_x : set nDraw pen offset x-axis coordinate
int pen_offset_y : set nDraw pen offset y-axis coordinate
Example Code for Setting Pen Type:
nDrawHelper. NDrawSetPenType ( 0 ); //Pencil
nDrawHelper. NDrawSetPenType ( 1 ); //Fountain Pen
nDrawHelper. NDrawSetPenType ( 2 ); //Chinese Brush
Return Parameter
void
NDrawSetPenType( int penType)
int penType: 0, 1, 2 set pen type as Pencil, Fountain Pen, and Chinese Brush
respectively
13
Rev. 1.3
Programing Guide for MobiScribe eNote
Example Code for Setting nDraw Paint Stroke Width:
nDraw and paint strokeWidth of the Canvas must be the same.
nDrawHelper.NDrawSetStrokeWidth(( int ) lineWidth );
paint .setStrokeWidth(( float ) lineWidth );
Return Parameter
void NDrawSetStrokeWidth( int lineWidth )
int lineWidth : set nDraw pen thickness
Example Code for Setting Pen Thickness with Pressure of Writing:
//not using pressure, NOTSET=-1
nDrawHelper.NDrawSetMaxStrokeWidthWhenUsingPressure( NOTSET );
//using pressure
nDrawHelper.NDrawSetMaxStrokeWidthWhenUsingPressure( max_pressure );
Return Parameter
void NDrawSetMaxStrokeWidthWhenUsingPressure( int max_pressure )
int max_pressure : set maximum pen thickness when using pressure
Example Code for Setting nDraw Paint Color:
nDraw and paint color of the canvas must be the same.
nDrawHelper.NDrawSetPaintColor(Color. BLACK );
paint .setColor(Color. BLACK );
Return Parameter
void NDrawSetPaintColor( int color )
int color : set nDraw paint color
14
Rev. 1.3
Programing Guide for MobiScribe eNote
Example Code for Changing Update Mode of Writing
public static boolean isPenUpdateModeDU() {
return SystemProperties.get( "ro.product.hardwareType" , "" ).equals( "ED0Q00" );
}
// update_mode initial
if (isPenUpdateModeDU()) {
setRefreshMode( UPDATE_MODE_PARTIAL_DU );
} else {
setRefreshMode( UPDATE_MODE_PARTIAL_A2 );
}
public void setRefreshMode( int refresh_mode) {
refreshMode = refresh_mode;
nDrawHelper.NDrawSetUpdateMode(refresh_mode);
}
Return Parameter
boolean isPenUpdateModeDU()
no parameter; it will return true if device is "ED0Q00"
void setRefreshMode(int refresh_mode)
int refresh_mode: set update mode for writing
void NDrawSetUpdateMode(int refresh_mode)
int refresh_mode: set update mode of writing
15
Rev. 1.3
Programing Guide for MobiScribe eNote
Example Code for Setting Orientation of Writing Area
private NtxView myNewView ;
myNewView .setCurrentRotation(getRequestedOrientation());
…
public void setCurrentRotation( int mCurrent_Rotation) {
this . mCurrentRotation = mCurrent_Rotation;
nDrawHelper. NDrawSetInputRotation ( mCurrentRotation );
}
Return Parameter
void NDrawSetInputRotation(int mCurrentRotation );
int mCurrentRotation: set orientation of writing area
Example Code for Taking Screenshots of Writing Area and Entire Screen
nDrawHelper.NDrawSaveSignature();
Return Parameter
void
NDrawSaveSignature()
no parameter; take screenshots of writing area and entire screen, and save
them(.png) to root directory of SD-Card.
Example Code for Setting Time Duration of Dropping Frames
nDrawHelper.NDrawDropFrames(400000000);
Return Parameter
void NDrawDropFrames( long nanosecond )
long nanosecond: set time duration of dropping frames.
16
Rev. 1.3
Programing Guide for MobiScribe eNote
Appendix
Refresh Control Broadcast Receiver
In order to improve EPD scrolling display performance, we have adopted some global eink
control strategies in framework. To ensure that your apps would not be influenced by those
strategies, you have to change eink control permission value, example code is as follows.
Example Code for Changing Eink Control Permission Value
@Override
protected void onResume() {
super .onResume();
changeEinkControlPermission( true );//not be influenced by strategies in framework
}
@Override
protected void onPause() {
super .onPause();
changeEinkControlPermission( false );//be influenced by strategies in framework
}
private void changeEinkControlPermission( boolean isForNtxAppsOnly) {
Intent changePermissionIntent = new Intent( "ntx.eink_control.CHANGE_PERMISSON" );
changePermissionIntent.putExtra( "isPermissionNtxApp" , isForNtxAppsOnly);
sendBroadcast(changePermissionIntent);
}
17
Rev. 1.3
Programing Guide for MobiScribe eNote
Using Intent( "ntx.eink_control. GLOBAL_REFRESH " ), we can switch to and keep one
refresh mode until we send broadcast to switch to another one. Example codes are as follows.
Example Code for Changing Global Refresh Mode to
UPDATE_MODE_GLOBAL_FULL_A2_WITH_DITHER
Intent myIntent = new Intent( "ntx.eink_control. GLOBAL_REFRESH " );
myIntent.putExtra( "updatemode" ,NtxView. UPDATE_MODE_GLOBAL_FULL_A2_WITH_DIT
HER );
myIntent.putExtra( "commandFromNtxApp" , true );
sendBroadcast(myIntent);
Example Code for Changing Global Refresh Mode to
UPDATE_MODE_GLOBAL_RESET
Intent myIntent = new Intent( "ntx.eink_control. GLOBAL_REFRESH " );
myIntent.putExtra( "updatemode" ,NtxView. UPDATE_MODE_GLOBAL_RESET );
myIntent.putExtra( "commandFromNtxApp" , true );
sendBroadcast(myIntent);
Using Intent( "ntx.eink_control. QUICK_REFRESH " ), we can temporarily do some
refresh, especially Full Refresh, just one time and then restore refresh mode immediately.
Example code is as follows.
Example Code for Doing a Full Refresh
Intent myIntent = new Intent( "ntx.eink_control. QUICK_REFRESH " );
myIntent.putExtra( "updatemode" ,NtxView. UPDATE_MODE_FULL_GC16 );
myIntent.putExtra( "commandFromNtxApp" , true );
sendBroadcast(myIntent);
18
Rev. 1.3
Programing Guide for MobiScribe eNote
Sometimes we want to drop animation frames to decrease eink flash, example codes are as
follows.
Example Code for Setting Time Duration of Dropping Frames
Intent myIntent = new Intent( "ntx.eink_control.DropFrames" );
myIntent.putExtra( "period" , 1900000000L );//1.9 Seconds
myIntent.putExtra( "commandFromNtxApp" , true );
sendBroadcast(myIntent);
Example Code for Resetting Time Duration of Dropping Frames
Intent myIntent = new Intent( "ntx.eink_control.DropFrames" );
myIntent.putExtra( "period" , 0L );//0 Seconds
myIntent.putExtra( "commandFromNtxApp" , true );
sendBroadcast(myIntent);
19
Rev. 1.3
Programing Guide for MobiScribe eNote
2-Step-Suspend
This feature is a method for power saving on the MobiScribe products
with eInk panel. When this function is enabled, the system will keep
the current screen and enter suspend after the user has not acted for
a while. Please refer to the following sample code to control this
function.
NOTE : When the user has an action, the system will be woken up
from the suspend and it will take a very short time. It’s suitable for
reader apps to flip pages. There will be a delay in the first stroke if
using this feature in the handwriting apps.
/**
* Control the 2-Step-Suspend for MobiScribe eInk devices
*
* @param state
* 1 is enable.
* 0 is disable.
*/
private void PowerEnhanceSet(int state ) {
try {
Settings.System.putInt(mContext.getContentResolver(), "power_enhance_enable", state );
} catch (Exception e) {
e.printStackTrace();
}
}
NOTE : Add the permission “WRITE_SETTINGS” in the AndroidManifest.xml
<uses-permission android:name="android.permission.WRITE_SETTINGS" />
20
Rev. 1.3
Programing Guide for MobiScribe eNote
adb Setup
1. USB Driver Installation for Windows 7
Download the driver from the Link , and install it.
2. Download Android SDK platform-tools
To visit the Android Studio website , USER GUIDE -> Release
Note -> SDK Platform Tools , and to download the platform-tools
package.
3. Add Android Vendor ID
● Windows 7 64-bit above version:
○ Make a “.android” directory at C:\Users\(user-name)\
○ Create a file “adb_usb.ini” in “.android” directory. Add the
content : 0x1f85, then save and close it.
● Ubuntu 14.05 64-bit version:
○ Make a “.android” directory at /home/(user-name)/
○ Create a file “adb_usb.ini” in “.android” directory. Add the
content : 0x1f85, then save and close it.
4. Execute adb server
Plug the usb link of your device to the computer. Extract the
download platform-tools package. Open the command line shell.
Execute the following commands :
adb kill-server
adb start-server
adb devices
21
Rev. 1.3
Programing Guide for netronix eNote
Eink Features/Waveforms
Standard(4 bits) Waveform Modes
Mode Description
INIT
(Global update WF)
Initialize
is used to completely clear the display, if it's left in an unknown state (i.e.
if the previous image has been lost by a re-boot)
DU
(Local update WF)
Direct update
Non-flashing waveform that can be used to update. It can update any
changed graytone pixel to black or white only. This waveform can be used
for pen or other fast menu updates. It only updates changed pixels.
GC
(Global update WF)
Grayscale Clear, 16 Levels
A “flashy” waveform used for 16 level grayscale images. This provides the
best image appearance. All the pixels are updated or cleared.
A2
(Local update WF)
Animation, 2 Levels
is a non-flashing waveform that can be used for fast updates and simple
animation. This waveform support black & white updates only. Image
quality and ghosting is reduced in exchange for the quicker response time.
It only updates changed pixels and recommend a white image transition
from 4bit to 1bit image into A2 and 1bit to 4bit out of A2 mode.
GL
( Local update WF
when white to
white, Global update
when 16 gray levels)
The GL waveform is used to update anti-aliased text with reduced flash.
GL should be used only with Full Display Update (UPD_FULL), the entire
display except pixels staying in white will update as the new image is
written. The GL waveform has 16 unique gray levels.
22
Rev. 1.3
Programing Guide for MobiScribe eNote
Regal (5 bits) Waveform Modes
Standard WF mode included
Mode Description
GLR*
(Local update WF)
Grayscale Local, 16 Levels, Regal
A variation of the GL low flash waveform with improved edge ghost
performance. 16 grayscale levels used for anti-aliased text update and
other images with white background. This should be used with full display
update. All pixels except pixels staying white will update.
GLD*
(Local update WF)
Grayscale Local, 16 Levels
A variation of the GL low flash waveform with improved edge and areal
ghost performance. 16 grayscale levels used for anti-aliased text update
and other images with white background. This should be used with full
display update. All pixels except pixels staying white will update.
23
Rev. 1.3
Programing Guide for MobiScribe eNote
UI Design with waveform mode
Features Example Use Cases
WF
mode
Comments
CONTENT
FOCUSED
Text Page updates
● Clock/digit time
● Calendar
● Turning pages in a book
or document
Regal,
GL
●Quality content with reduced
flashiness during page updates;
●Primarily for White background
●May need GC update periodically
Pictures
● Launcher page turn
● Gallery
● Book covers
● Screen savers
● Illustrations
● Home screens
GC ●When you want best image quality
●More flashing but less ghosting
Entering a new
book/application,
natural user
transitions
●After a submenu/book
has been selected from a
menu or going from
submenu/book to menu
●Menu to submenu
●Choosing between
applications
GC
●Take advantage of natural breaks in
the information flow to use GC to
clear ghost buildup
●When changing menus on screen
INFORMATIO
NAL
Icons flashing or
updating
Battery charging, page
loading, WiFi connection,
download, etc.
DU
●Use black and white icons to take
advantage of faster update time with
DU mode
●Can be small in a title/header bar area
or Large in middle of display
Overlaying content
(pop-up menus,
notifications, dialog
boxes)
●Low battery warning
●Confirming a user
decision
●Quick view
DU
For best quality, bring up dialog/pop-up
with DU and erase dialog/pop-up with
GC.
STARTUP/
SHUTDOWN
First time device is
Powered-on, Loss
of power, System
freeze
When display is in
unknown state
INIT
●Longest update time and restarts
display
●Not a DC balanced waveform so use
sparingly
Startup from
known image
● Coming out of screen
saver
● Starting from known
shut-down state
GC
Because image is known, do not need to
use INIT
Device Shutdown;
Sleep mode
Power down with image
on the display
GC
Recommendation to go into a known
state
24
Rev. 1.3
Programing Guide for MobiScribe eNote
Features Example Use Cases
WF
mode
Comments
INTERACTIVE
Menu Selection
(response to user
input)
Black/White options: Radio
buttons, underlining, Box
outline, invert text etc.
DU
Grayscale option: Highlight
selection in gray
GC Box selection; Could be more flashy
Keyboard/Text
input
Search box entry Password
entry
DU
If anti-aliased text is desired, can play
around with combined DU/GC update
Keyboard response
Button key
acknowledgement
DU
Inversion of selected key Use inherent
flashiness of GC to redisplay the
button?
Browsing books,
apps, or menu
options (graphical)
Selection GC
Recommend doing pipeline to show
transitions
Fast Page Turn
Scanning through
Books/Documents
A2
Reduced contrast - may be more
ghosty; Need to start and end with
White; GC should follow to display a
quality image
DU Not as fast as A2 Better image quality
Panning
Navigating zoomed in
text/images
Navigating maps Camera
view finder
A2 Achieve grayscale through dithering
Highlighting Text highlighting
DU
GC
DU+GC
inversion of text with DU
Grayscale highlighting with GC
Combination of both: DU for fast initial
user response, followed by GC to
change to grayscale?
25
Rev. 1.3
