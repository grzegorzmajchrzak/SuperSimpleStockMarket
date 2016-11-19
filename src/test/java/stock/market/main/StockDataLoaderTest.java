package stock.market.main;


import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import stock.market.impl.StockOperations;
import stock.market.impl.TradeStorage;

import java.io.File;
import java.nio.file.Files;
import java.util.Map;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class StockDataLoaderTest {
    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();
    private File tempFile;

    private StockDataLoader dataLoader;
    @Mock
    private TradeStorageFactory tradeStorageFactory;

    @Before
    public void setUp() throws Exception {
        tempFile = folder.newFile();
        dataLoader = new StockDataLoader(tempFile.toURI(), tradeStorageFactory);

        when(tradeStorageFactory.create()).thenReturn(mock(TradeStorage.class));
    }

    @Test
    public void shouldLoadData() throws Exception {
        //given
        String fileContent = "Stock Symbol,Type,Last Dividend,Fixed Dividend,Par Value\n"
                + "TEA,Common,0,,100";
        Files.write(tempFile.toPath(),fileContent.getBytes());
        //when
        Map<String, StockOperations> result = dataLoader.loadData();
        //then
        assertThat(result).hasSize(1);
        assertThat(result.get("TEA")).isNotNull();
        verify(tradeStorageFactory, times(1)).create();
    }

    @Test
    public void shouldReportProblemIfFileCannotBeParsed() throws Exception {
        String fileContent = "Stock Symbol,Type,Last Dividend,Fixed Dividend,Par Value\n"
                + "TEA,Unknown,0,,100";
        Files.write(tempFile.toPath(),fileContent.getBytes());

        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("cannot load data from source");

        dataLoader.loadData();
    }
}