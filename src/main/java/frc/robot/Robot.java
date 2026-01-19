// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import java.util.function.DoubleConsumer;

import Team4450.Lib.*;

import com.ctre.phoenix6.CANBus;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private final CANBus canbus = new CANBus("rio");
  private final TalonFX m_fx = new TalonFX(10, canbus);
  //private final TalonFX m_fllr = new TalonFX(11, "rio");

  private final XboxController m_joystick = new XboxController(0);

  public double slot0_kP = 0.20; // An error of 1 rotation per second results in 0.20 V output
  
  DoubleConsumer setkP = kP -> setSlot0_kP(kP);

  TalonFXVelocityController   tfxController = new TalonFXVelocityController(m_fx, "Shooter")
          .withVoltagekP(.30)
          .withInverted(true)
          .withNeutralMode(NeutralModeValue.Brake);

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  public Robot() {
    try {
      Util.CustomLogger.setup("robot.");
    } catch (Exception e) {}  
  }

  @Override
  public void robotPeriodic() {
    try {
      CommandScheduler.getInstance().run();
    } catch (Exception e) {
      Util.logException(e);
      this.endCompetition();
    }
  }

  @Override
  public void autonomousInit() {}

  @Override
  public void autonomousPeriodic() {}

  @Override
  public void teleopInit() {}

  @Override
  public void teleopPeriodic() {
    if (m_joystick.getXButtonPressed()) tfxController.stop();

    if (m_joystick.getBButtonPressed()) tfxController.desiredRPM(2300);
  }

  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {}

  @Override
  public void testInit() {
    // Cancels all running commands at the start of test mode.

    CommandScheduler.getInstance().cancelAll();

    // Next two lines launch teleop mode, but since we are in test
    // mode, LiveWindow will be enabled to display test data to the
    // outlineviewer and shuffleboard. Our "test" mode is the regular
    // telop with LW enabled. Our code displays more detailed test/debug
    // data in LW mode.

    LiveWindow.enableAllTelemetry();

    teleopInit();

    CommandScheduler.getInstance().enable();
  }

  @Override
  public void testPeriodic() { 
    teleopPeriodic();
  }

  @Override
  public void simulationInit() {
  }

  @Override
  public void simulationPeriodic() {
  }

  public void setSlot0_kP(double kP) {
    slot0_kP = kP;
  }
}
 