/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.filename.comparator;

import java.io.File;
import java.util.Comparator;

/**
 * @author Krishna
 */
public class FileNameComparator implements Comparator<File>
{

	@Override
	public int compare(final File file, final File otherFile)
	{

		return Long.compare(extractInt(file), extractInt(otherFile));
	}

	private long extractInt(final File file)
	{
		final String fileName = file.getName();
		final String num = fileName.replaceAll("\\D", "");
		// return 0 if no digits found
		return num.isEmpty() ? 0L : Long.parseLong(num);
	}
}
