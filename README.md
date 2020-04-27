# Autonomous Car Simulator

This project is supposed to provide a control system testing environment for a
four wheeled vehicle. It uses a real time dynamic simulation of a "bicycle model", as well as a realistic tire model

PID control demonstration

![Image](https://j.gifs.com/jZK3QP.gif)

MPC control demonstration

![Image](https://j.gifs.com/vlv38r.gif)

Pink dots represent the path points predicted by the optimizer. It doesn't work that well, because the COBYLA optimizer isn't really suited for
this kind of application. This kind of performance was only made possible by a simplifying the MPC itself, e.g. embedding kinematic constraints in the cost function. Interfacing with IPOPT could probably solve this issue.

## Vehicle model equations

Following equations represent the "bicycle" vehicle model in which two front and
two rear wheel are turned into a one front and one rear wheel, with disregard to the
roll of the vehicle. Such simplification gives accurate results while also being
relatively easy to implement. The model equations are integrated using the RK4 method

<img src="https://i.imgur.com/lSApeRQ.png" width="580" height="500" />

Symbols explained:
  - psi - yaw angle in the global frame
  - omega - yaw rate
  - Iz - Inertia about the centre of rotation of the vehicle around the z axis
  - Lf - length from the front of the vehicle to the center of gravity
  - Lr - length from the rear of the vehicle to the center of gravity
  - delta - steering angle
  - vx, vy - longitudinal and lateral velocity in vehicle reference frame
  - X, Y - vehicle coordinates in the global reference frame
  - If, Ir - inertia of the front and rear tyre along the axis of rotation
  - Rf, Rr - radius of the front and rear tyre
  - Tf, Tr - torque on the front and rear wheel from the engine

## Tyre model

The whole paper describing the tyre model and all it's variables and constants can be found here ["Analytical Modeling of Driver Response in Crash Avoidance Maneuvering Volume II: An
Interactive Tire Model for Driver/Vehicle Simulation"](https://babel.hathitrust.org/cgi/pt?id=mdp.39015075196983&view=1up&seq=1)

![Imgur](https://i.imgur.com/IFcefMR.png)
