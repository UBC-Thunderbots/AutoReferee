/*
 * Copyright (c) 2009 - 2020, DHBW Mannheim - TIGERs Mannheim
 */

package edu.tigers.sumatra.drawable;

import com.sleepycat.persist.model.Persistent;
import edu.tigers.sumatra.math.line.ILineSegment;
import edu.tigers.sumatra.math.line.Lines;
import edu.tigers.sumatra.math.vector.IVector2;
import edu.tigers.sumatra.math.vector.Vector2f;

import java.awt.Color;
import java.awt.Graphics2D;


/**
 * This is a Line connected to a color
 */
@Persistent
public class DrawableLine extends ADrawableWithStroke
{
	private final ILineSegment line;


	@SuppressWarnings("unused")
	private DrawableLine()
	{
		line = Lines.segmentFromPoints(Vector2f.ZERO_VECTOR, Vector2f.ZERO_VECTOR);
	}


	/**
	 * Drawable line from normal line
	 *
	 * @param line  to draw
	 * @param color of this line
	 */
	public DrawableLine(final ILineSegment line, final Color color)
	{
		this.line = line;
		setColor(color);
	}


	/**
	 * Drawable line from normal line
	 *
	 * @param line to draw
	 */
	public DrawableLine(final ILineSegment line)
	{
		this(line, Color.black);
	}


	/**
	 * Drawable line from normal start and end
	 *
	 * @param start of line to draw
	 * @param end   of line to draw
	 * @param color of this line
	 */
	public DrawableLine(IVector2 start, IVector2 end, Color color)
	{
		this.line = Lines.segmentFromPoints(start, end);
		setColor(color);
	}


	/**
	 * Drawable line from normal start and end
	 *
	 * @param start of line to draw
	 * @param end   of line to draw
	 */
	public DrawableLine(IVector2 start, IVector2 end)
	{
		this(start, end, Color.BLACK);
	}


	@Override
	public void paintShape(final Graphics2D g, final IDrawableTool tool, final boolean invert)
	{
		super.paintShape(g, tool, invert);

		// draw line
		final IVector2 lineStart = tool.transformToGuiCoordinates(line.supportVector(), invert);
		final IVector2 lineEnd = tool.transformToGuiCoordinates(line.directionVector().addNew(line.supportVector()),
				invert);
		g.drawLine((int) lineStart.x(), (int) lineStart.y(), (int) lineEnd.x(), (int) lineEnd.y());
	}
}
