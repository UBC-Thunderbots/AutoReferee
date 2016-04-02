/*
 * *********************************************************
 * Copyright (c) 2009 - 2016, DHBW Mannheim - Tigers Mannheim
 * Project: TIGERS - Sumatra
 * Date: Feb 14, 2016
 * Author(s): "Lukas Magel"
 * *********************************************************
 */
package edu.tigers.autoreferee.engine.violations.impl;

import java.util.List;
import java.util.Optional;

import edu.tigers.autoreferee.IAutoRefFrame;
import edu.tigers.autoreferee.engine.violations.IRuleViolation;
import edu.tigers.sumatra.wp.data.EGameStateNeutral;


/**
 * Abstract implementation which provides a prepare method to do initial work
 * 
 * @author "Lukas Magel"
 */
public abstract class APreparingViolationDetector extends AViolationDetector
{
	
	private boolean	firstUpdate	= true;
	
	
	/**
	 * @param gamestate
	 */
	public APreparingViolationDetector(final EGameStateNeutral gamestate)
	{
		super(gamestate);
	}
	
	
	/**
	 * @param gamestates
	 */
	public APreparingViolationDetector(final List<EGameStateNeutral> gamestates)
	{
		super(gamestates);
	}
	
	
	@Override
	public final Optional<IRuleViolation> update(final IAutoRefFrame frame, final List<IRuleViolation> violations)
	{
		if (firstUpdate)
		{
			prepare(frame);
			firstUpdate = false;
		}
		return doUpdate(frame, violations);
	}
	
	
	protected abstract void prepare(IAutoRefFrame frame);
	
	
	protected abstract Optional<IRuleViolation> doUpdate(IAutoRefFrame frame, List<IRuleViolation> violations);
	
	
	@Override
	public final void reset()
	{
		firstUpdate = true;
		doReset();
	}
	
	
	protected void doReset()
	{
	}
	
}