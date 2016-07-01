/*
 * *********************************************************
 * Copyright (c) 2009 - 2016, DHBW Mannheim - Tigers Mannheim
 * Project: TIGERS - Sumatra
 * Date: Jun 30, 2016
 * Author(s): "Lukas Magel"
 * *********************************************************
 */
package edu.tigers.autoreferee;

import java.util.ArrayList;
import java.util.List;

import edu.tigers.autoreferee.engine.calc.BallLeftFieldCalc;
import edu.tigers.autoreferee.engine.calc.BotLastTouchedBallCalc;
import edu.tigers.autoreferee.engine.calc.GameStateHistoryCalc;
import edu.tigers.autoreferee.engine.calc.IRefereeCalc;
import edu.tigers.autoreferee.engine.calc.LastStopBallPositionCalc;
import edu.tigers.autoreferee.engine.calc.PossibleGoalCalc;
import edu.tigers.sumatra.wp.data.WorldFrameWrapper;


/**
 * @author "Lukas Magel"
 */
public class AutoRefFramePreprocessor
{
	private List<IRefereeCalc>	calculators	= new ArrayList<>();
	
	private IAutoRefFrame		lastFrame;
	
	
	/**
	 * 
	 */
	public AutoRefFramePreprocessor()
	{
		calculators.add(new BallLeftFieldCalc());
		calculators.add(new BotLastTouchedBallCalc());
		calculators.add(new GameStateHistoryCalc());
		calculators.add(new PossibleGoalCalc());
		calculators.add(new LastStopBallPositionCalc());
	}
	
	
	/**
	 * @param wframe
	 * @return
	 */
	public AutoRefFrame process(final WorldFrameWrapper wframe)
	{
		AutoRefFrame frame = new AutoRefFrame(lastFrame, wframe);
		
		/*
		 * We can only run the calculators if we have a last frame.
		 */
		if (lastFrame != null)
		{
			runCalculators(frame);
		}
		
		setLastFrame(frame);
		return frame;
	}
	
	
	/**
	 * @param frame
	 */
	public void setLastFrame(final WorldFrameWrapper frame)
	{
		setLastFrame(new AutoRefFrame(null, frame));
	}
	
	
	private void setLastFrame(final IAutoRefFrame frame)
	{
		if (lastFrame != null)
		{
			lastFrame.cleanUp();
		}
		lastFrame = frame;
	}
	
	
	/**
	 * 
	 */
	public void clear()
	{
		lastFrame = null;
	}
	
	
	private void runCalculators(final AutoRefFrame frame)
	{
		for (IRefereeCalc calc : calculators)
		{
			calc.process(frame);
		}
	}
	
	
	/**
	 * @return
	 */
	public boolean hasLastFrame()
	{
		return lastFrame != null;
	}
}