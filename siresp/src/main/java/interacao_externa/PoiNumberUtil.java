package interacao_externa;


import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFCell;

public final class PoiNumberUtil {

    private PoiNumberUtil() {}

    /** 
     * Se a célula for NUMERIC, devolve um texto sem notação científica.
     * Tenta usar o rawValue do XSSFCell; se não der, usa NumberToTextConverter.
     * Retorna null se a célula não for numérica.
     */
    public static String numericCellAsPlainString(Cell cell) {
        if (cell == null) return null;
        CellType t = cell.getCellType();
        if (t == CellType.FORMULA) t = cell.getCachedFormulaResultType();
        if (t != CellType.NUMERIC) return null;

        // Tenta rawValue do XSSFCell (xlsx)
        if (cell instanceof XSSFCell) {
            String raw = ((XSSFCell) cell).getRawValue(); // texto conforme armazenado
            if (raw != null) return raw;
        }
        // Fallback: converter o double sem notação científica
        return NumberToTextConverter.toText(cell.getNumericCellValue());
    }
}

