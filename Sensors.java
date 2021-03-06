package org.firstinspires.ftc.teamcode.teamcode;

import android.text.method.Touch;

import com.disnodeteam.dogecv.CameraViewDisplay;
import com.disnodeteam.dogecv.DogeCV;
import com.disnodeteam.dogecv.detectors.roverrukus.GoldAlignDetector;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.DigitalChannel;


import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@Autonomous (name = "Sensors")
public class Sensors extends LinearOpMode {

    private GoldAlignDetector detector;
    private DcMotor left, right, hangingMotor; // declare drive motor variables

    private DistanceSensor rangeLeft, rangeRight; //declares range sensor variables

    private ColorSensor colorLeft, colorRight; //declares color sensor variables

    private DigitalChannel touchLeft, touchRight; //declares touch sensor variables

    private String position = null;

    private double powerOff = 0; //declares common powers that we use
@Override
    public void runOpMode() {
    //sets drive motors
    left = hardwareMap.dcMotor.get("left");
    right = hardwareMap.dcMotor.get("right");
    hangingMotor = hardwareMap.dcMotor.get("hangingMotor");
    //sets range sensors
    rangeLeft = hardwareMap.get(DistanceSensor.class, "rangeLeft");
    rangeRight = hardwareMap.get(DistanceSensor.class, "rangeRight");
    //sets color sensors
    colorLeft = hardwareMap.get(ColorSensor.class, "colorLeft");
    colorRight = hardwareMap. get(ColorSensor.class, "colorRight");
    //sets touch sensor variables
    touchLeft = hardwareMap.get(DigitalChannel.class, "touchLeft");
    touchRight = hardwareMap.get(DigitalChannel.class, "touchRight");

   //sets up drive motors to our specification
    left.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    right.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    left.setDirection(DcMotorSimple.Direction.REVERSE);

    touchRight.setMode(DigitalChannel.Mode.INPUT);
    touchLeft.setMode(DigitalChannel.Mode.INPUT);

    detector = new GoldAlignDetector();
    detector.init(hardwareMap.appContext, CameraViewDisplay.getInstance());
    detector.useDefaults();

    detector.alignSize = 10;
    detector.alignPosOffset = 5000;
    detector.downscale = 0.4;

    detector.areaScoringMethod = DogeCV.AreaScoringMethod.MAX_AREA;
    detector.maxAreaScorer.weight = 0.005;

    detector.ratioScorer.weight = 5;
    detector.ratioScorer.perfectRatio = 0.8;

    detector.setAlignSettings(0, 1000);
    detector.enable();
    waitForStart();

    HangingApparatus();
    DrivetoLine(50, 42, 0.3, 0.3,true);
    LineUp(50,42,0.2,0.2);
   // DistancetoRate(20, DistanceUnit.INCH, 10);
   // Straighten(DistanceUnit.INCH);

    }
    //returns whether the left range sensor is reading less than a certain value
    private boolean InRangeLeft(double target, DistanceUnit units){
    //creates a variable to hold the range sensor's reading
    double distance;
    //sets the distance variable to the value that the range sensor reads
    distance = rangeLeft.getDistance(units);
    //returns true if the robot is closer to the target than the target position
    return (distance <= target);
    }
    //returns whether the right range sensor is reading less than a certain value
    private boolean InRangeRight(double target, DistanceUnit units){
    //declares a variable to hold the range sensor's reading
    double distanceRight;
    //sets the distance variable to the value that the range sensor reads
    distanceRight = rangeRight.getDistance(units);
    //returns true if the robot is closer to the target than the target position
    return ( distanceRight <= target);
    }
    //powers drive motors until range sensor reads a certain target
    private void DriveUntilDistance(double target, DistanceUnit unit){
    //goes until ten sensor is ten units from the target
    while(!InRangeLeft(target +10, unit) ) {
        //powers drive motors
        left.setPower(0.5);
        right.setPower(0.5);
    }
    //goes until the sensor is within the range of the target
    while (!InRangeLeft(target, unit )){
        //sets drive power
        left.setPower(0.1);
        right.setPower(0.1);
    }
    //turns off drive motors
    left.setPower(powerOff);
    right.setPower(powerOff);
    }
    //converts the distance read by the range sensor to a speed for drive motors
    private void DistancetoRate(double stoptarget, DistanceUnit unit, double time){
    //sets variables to hold the values of the distance that the robot needs to travel
    double distanceRight, distanceLeft;
    //declares a variable to hold the linear rate
    double linearRateLeft, linearRateRight;
    //declares a variable to hold the angular rate
    double angularRateLeft, angularRateRight;
    //the radius of the wheel in inches
    double radius = 2;
    //runs until range sensor reads a specified distance at which point the robot stops
    while(!InRangeLeft(stoptarget, unit) && !InRangeRight(stoptarget, unit)){
        //sets the distance variables to the range sensor to the difference of the range sensor reading and target location
        distanceLeft = rangeLeft.getDistance(unit) - stoptarget;
        distanceRight = rangeRight.getDistance(unit) - stoptarget;
        //sets linear rate to the distance we want to travel divided by the target time
        linearRateLeft = distanceLeft / time;
        linearRateRight = distanceRight / time;
        //converts linear rate into angular rate by dividing it by the radius of the wheel
        angularRateLeft = linearRateLeft / radius;
        angularRateRight = linearRateRight / radius;
        //sets drive motors to the angular rate value
        left.setPower(angularRateLeft);
        right.setPower(angularRateRight);
    }
    //turns off drive motors
    right.setPower(powerOff);
    left.setPower(powerOff);
    //waits one second to give robot to fully stop
    sleep(1000);
    //if right wheel is further away than left wheel, turns the right wheel
    if(rangeRight.getDistance(unit ) > rangeLeft.getDistance(unit)){

        while(!InRangeLeft(rangeRight.getDistance(unit), unit)) {
            //turns right wheel
            right.setPower(0.1);
            }
        //turns off right drive motor
        right.setPower(powerOff);
        }
    //if left wheel is further away than right wheel, turns the left wheel
    if(rangeLeft.getDistance(unit) > rangeRight.getDistance(unit)){

        while(!InRangeRight(rangeLeft.getDistance(unit), unit)){
            //turns left wheel
            left.setPower(0.1);
            }
        //turns off left drive motor
        left.setPower(powerOff);
        }
    }
    //determines if the color sensor's reading is in between two values
    private boolean WithinColorRange(int max, int min, ColorSensor sensor){
    //declares and sets a variable equal to the color sensor reading
    int color = sensor.blue();
    //returns true if the color sensor is less than or equal to the max value and less than or equal to the min value
    return(color <= max && color >= min);

    }
    //uses color sensors to square along a colored line
    private void LineUp(int max, int min, double leftPower, double rightPower){
   //moves right wheel until it is on the color sensor reads the line's color range
    while(!WithinColorRange(max, min, colorRight)){
        //sets right drive power
        right.setPower(rightPower);
     }
     //turns off right drive power
    right.setPower(powerOff);
   //moves left motor until the color sensor reads the line's color range
    while(!WithinColorRange(max, min, colorLeft)){
            //sets left drive power
            left.setPower(leftPower);
        }
        //turns off left drive power
        left.setPower(powerOff);
    //moves left motor until the color sensor reads the non-line's color range
    while(!WithinColorRange(23, 11, colorLeft)){
        //sets left drive power
        left.setPower(leftPower);
    }
    //turns off left drive power
    left.setPower(powerOff);
    //moves right motor forward and left motor backwards until right motor reads the non-line's color range
    while(!WithinColorRange(23, 11, colorRight)){
        //sets drive motor's powers
        right.setPower(0.1);
        left.setPower(-0.1);
    }
    //turns off drive motors
    left.setPower(powerOff);
    right.setPower(powerOff);
    }

    //stops the robot when the color sensor reads within the set range
    private void DrivetoLine(int max, int min, double leftPower, double rightPower, boolean backup){
    //runs until it is within the set range
    while(!WithinColorRange(max, min, colorRight)){
        //sets drive motors to respective powers set in the calling of the method
        right.setPower(rightPower);
        left.setPower(leftPower);
        }
        //turns power off
        left.setPower(powerOff);
        right.setPower(powerOff);
        //if backup variable set in calling of the method is true
        if(backup){
            //back up until right behind the line we drove to
            while(!WithinColorRange(25,13, colorRight)){
                right.setPower(-0.2);
                left.setPower(-0.2);
            }
            //turns drive power off
            left.setPower(powerOff);
            right.setPower(powerOff);
        }
    }
    //uses the range sensor to square robot relative to a surface
    private void Straighten(DistanceUnit units){
    //declares variables to hold range sensor reading
    double rightRange, leftRange;
    //sets the initial values of the variables to the range sensors reading
    rightRange = rangeRight.getDistance(units);
    leftRange = rangeLeft.getDistance(units);
    //runs until we are straight
    while(rightRange != leftRange){
        //updates values of range variables
        rightRange = rangeRight.getDistance(units);
        leftRange = rangeLeft.getDistance(units);
        //turns right if the right distance is less than the left
        if(rightRange < leftRange){
            //sets motor powers
            right.setPower(-0.25);
            left.setPower(0.25);
        }
        //turns off motor power
        right.setPower(powerOff);
        left.setPower(powerOff);
        //turns left if the left range is less than the right
        if(leftRange < rightRange){
            //sets motor power
            left.setPower(-0.25);
            right.setPower(0.25);
        }
        //turns drive motor power off
        right.setPower(powerOff);
        left.setPower(powerOff);
    }

    }
    private boolean LeftPressed(){
    return !touchLeft.getState();
    }
    private boolean RightPressed(){
    return !touchRight.getState();
    }
    //a method that drives until both touch sensors are pressed
    private void DriveUntilTouch(){

    while (!LeftPressed() || !RightPressed()){
    telemetry.addData("left pressed: ", LeftPressed());
    telemetry.addData("right pressed: ", RightPressed());
    telemetry.update();
    left.setPower(-0.2);
    right.setPower(-0.2);

    }
    left.setPower(powerOff);
    right.setPower(powerOff);

    }
    private void HangingApparatus(){
        while(!RightPressed()){
            hangingMotor.setPower(1);
        }
        hangingMotor.setPower(powerOff);
    }

}


