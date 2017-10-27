/**
 *                    BioJava development code
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  If you do not have a copy,
 * see:
 *
 *      http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright for this code is held jointly by the individual
 * authors.  These should be listed in @author doc comments.
 *
 * For more information on the BioJava project and its aims,
 * or to join the biojava-l mailing list, visit the home page
 * at:
 *
 *      http://www.biojava.org/
 *
 * Created on Nov 28, 2012
 * Created by Andreas Prlic
 *
 * @since 3.0.2
 */
package org.biojava.nbio.structure;


import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.biojava.nbio.structure.align.util.AtomCache;
import org.biojava.nbio.structure.io.FileParsingParameters;
import org.junit.Test;
import static org.junit.Assert.*;

public class TestCloning {

	@Test
	public void test1a4wCloning() throws StructureException, IOException {

		Structure s;

		AtomCache cache = new AtomCache();
		FileParsingParameters params = new FileParsingParameters();
		params.setAlignSeqRes(true);
		cache.setFileParsingParams(params);

		StructureIO.setAtomCache(cache);

		s = StructureIO.getStructure("1a4w");

		Structure c = s.clone();

		compareCloned(s,c);

	}



	@Test
	public void testAsymUnitCloning() throws StructureException, IOException {

		Structure s;


		AtomCache cache = new AtomCache();
		FileParsingParameters params = new FileParsingParameters();
		params.setAlignSeqRes(false);
		cache.setFileParsingParams(params);

		StructureIO.setAtomCache(cache);

		s = StructureIO.getStructure("1stp");

		Structure c = s.clone();

		compareCloned(s,c);
	}

	@Test
	public void testBioUnitCloning() throws StructureException, IOException {

		Structure s;
		s = StructureIO.getBiologicalAssembly("1stp",1);

		Structure c = s.clone();

		compareCloned(s,c);

	}

	/**
	 * A Structure with alt locs, we make sure they are being cloned too
	 * @throws StructureException
	 * @throws IOException
	 */
	@Test
	public void test3piuCloning() throws StructureException, IOException {

		AtomCache cache = new AtomCache();
		FileParsingParameters params = new FileParsingParameters();
		params.setAlignSeqRes(true);
		cache.setFileParsingParams(params);

		StructureIO.setAtomCache(cache);

		Structure s = StructureIO.getStructure("3piu");

		Structure c = s.clone();

		compareCloned(s, c);
	}

	private void compareCloned(Structure s, Structure c) throws StructureException {

		assertEquals(s.getChains().size(), c.getChains().size());

		for ( Chain chain : s.getChains()) {

			Chain test = c.getChain(chain.getId());

			assertEquals("Could not correctly clone seqres for chain " + chain.getId() , chain.getSeqResLength(),test.getSeqResLength());

			assertEquals("Could not correctly clone atom records for chain " + chain.getId() , chain.getAtomLength(),test.getAtomLength());

			Iterator<Group> it = test.getAtomGroups().iterator();
			for (Group g : chain.getAtomGroups()) {
				Group testGroup = it.next();
				//if (g.hasAltLoc()) {
				//	System.out.println(g.toString());
				//}
				assertEquals(g.getAltLocs().size(), testGroup.getAltLocs().size());
			}
			
			it = test.getSeqResGroups().iterator();
			for (Group g: chain.getSeqResGroups()) {
				Group testGroup = it.next();
				assertEquals(g.getAltLocs().size(), testGroup.getAltLocs().size());
			}
		}

		Atom[] allAtoms = StructureTools.getAllAtomArray(s);

		Atom[] allAtomsCloned = StructureTools.getAllAtomArray(c);

		assertEquals(allAtoms.length,allAtomsCloned.length);

	}
	
	@Test
    public void testBondCloning() throws IOException, StructureException {

            AtomCache cache = new AtomCache();
            cache.setUseMmCif(true);

            FileParsingParameters params = cache.getFileParsingParams();
            params.setCreateAtomBonds(true);
            cache.setFileParsingParams(params);

            Structure s = cache.getStructure("2I13");
            List<Bond> bonds = s.getNonPolyChain("G").getAtomGroup(0).getAtom(0).getBonds();
            assertNotNull(bonds);

            Structure s2 = s.clone();
            bonds = s2.getNonPolyChain("G").getAtomGroup(0).getAtom(0).getBonds();
            assertNotNull(bonds);
    }

}
