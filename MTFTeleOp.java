package org.firstinspires.ftc.teamcode.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "MTF TeleOp", group = "Default")
public class MTFTeleOp extends LinearOpMode {

    private DcMotor left, left1, right1, right, hangingMotor, pivotMotor, slideMotor; //declares
    private DigitalChannel touchLower, touchUpper;
    private CRServo collector;

    private boolean collecting;
@Override
    public void runOpMode()
{
    left = hardwareMap.dcMotor.get("left"); //sets value to left motor
    right = hardwareMap.dcMotor.get("right"); //sets value to right motor
    left1 = hardwareMap.dcMotor.get(("left1"));
    right1 = hardwareMap.dcMotor.get("right1");
    hangingMotor = hardwareMap.dcMotor.get("hangingMotor");
    touchLower = hardwareMap.get(DigitalChannel.class, "touchLeft");
    touchUpper = hardwareMap.get(DigitalChannel.class, "touchRight");
    pivotMotor = hardwareMap.dcMotor.get("pivotMotor");
    slideMotor = hardwareMap.dcMotor.get("slideMotor");
    collector = hardwareMap.crservo.get("collector");





    touchLower.setMode(DigitalChannel.Mode.INPUT);
    touchUpper.setMode(DigitalChannel.Mode.INPUT);


    left.setDirection(DcMotorSimple.Direction.REVERSE);
    left1.setDirection(DcMotorSimple.Direction.REVERSE);
    hangingMotor.setDirection(DcMotorSimple.Direction.REVERSE);

    waitForStart();
    while (opModeIsActive())
    {
        left.setPower(gamepad1.left_stick_y); //sets left motor power
        left1.setPower(gamepad1.left_stick_y);
        right.setPower(gamepad1.right_stick_y); //sets right motor power
        right1.setPower(gamepad1.right_stick_y);

        if(gamepad2.x)
        {
            if (collecting){
                collecting = false;
                collector.setPower(0);
            }
            else if (!collecting){
                collecting = true;
                collector.setPower(1);
            }
    }

        slideMotor.setPower(gamepad2.right_stick_y);
        pivotMotor.setPower(gamepad2.left_stick_y);

        hangingMotor.setPower(gamepad1.left_trigger);
        hangingMotor.setPower(-gamepad1.right_trigger);

        idle();
    }
}








}
