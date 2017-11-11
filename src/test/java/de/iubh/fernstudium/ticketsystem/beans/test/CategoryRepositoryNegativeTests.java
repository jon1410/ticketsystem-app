package de.iubh.fernstudium.ticketsystem.beans.test;

import de.iubh.fernstudium.ticketsystem.beans.utils.FacesContextUtils;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
@PrepareForTest(FacesContextUtils.class)
public class CategoryRepositoryNegativeTests {

    //eigene Testklasse, weil statisches Mocken notwendig, Needle funktioniert nicht mit PowerMock



}
