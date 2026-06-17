package interacao_externa;

import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import modelosDados.ExcelColumn;

import org.apache.poi.ss.usermodel.DateUtil;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;

public class ExcelBinder {

	
	public static <T> ArrayList<T> readSheet(
            InputStream in,
            Class<T> type,
            int sheetPosition,
            int headerRowIndex,
            boolean skipEmptyRows
    ) {
		try (Workbook wb = new XSSFWorkbook(in)) {
            Sheet sheet = wb.getSheetAt(0);
            
           
            if (sheet == null) {
                throw new IllegalArgumentException("Planilha não encontrada: " + sheetPosition);
            }

            // Mapear cabeçalho -> índice
            Row headerRow = sheet.getRow(headerRowIndex);
            if (headerRow == null) {
                throw new IllegalArgumentException("Linha de cabeçalho não encontrada no índice " + headerRowIndex);
            }
            Map<String, Integer> headerIndex = readHeaderIndex(headerRow);

            // Preparar metadados dos campos anotados
            List<FieldBinding> bindings = prepareBindings(type, headerIndex);

            FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();
            DataFormatter formatter = new DataFormatter(Locale.getDefault());

            ArrayList<T> result = new ArrayList<>();
            for (int r = headerRowIndex + 1; r <= sheet.getLastRowNum(); r++) {
                Row row = sheet.getRow(r);
                if (row == null) {
                    if (!skipEmptyRows) result.add(type.getDeclaredConstructor().newInstance());
                    continue;
                }

                // Se quiser pular linhas completamente vazias:
                if (skipEmptyRows && isRowCompletelyEmpty(row)) continue;

                T instance = type.getDeclaredConstructor().newInstance();

                for (FieldBinding fb : bindings) {
                    Cell cell = getCell(row, fb.colIndex);
                    Object value = convertCellValue(cell, evaluator, formatter, fb);
                    if (value == null && fb.required) {
                        throw new IllegalStateException(
                                "Campo obrigatório vazio: " + fb.field.getName() +
                                " (linha " + (r + 1) + ", coluna " + (fb.colIndex + 1) + ")"
                        );
                    }
                    if (value != null) {
                        fb.field.setAccessible(true);
                        fb.field.set(instance, value);
                    }
                    else
                    {
                        fb.field.setAccessible(true);
                        fb.field.set(instance, "");
                    }
                }

                result.add(instance);
            }

            return result;
        } catch (RuntimeException re) {
            throw re;
        } catch (Exception e) {
            throw new RuntimeException("Falha ao ler a planilha: " + e.getMessage(), e);
        }

	}
	
    public static <T> ArrayList<T> readSheet(
            InputStream in,
            Class<T> type,
            String sheetName,
            int headerRowIndex,
            boolean skipEmptyRows
    ) {

			ZipSecureFile.setMinInflateRatio(0.001);
			
			// (Opcional) limite total de bytes descompactados
			ZipSecureFile.setMaxEntrySize(500 * 1024 * 1024); // 500 MB
			ZipSecureFile.setMaxTextSize(200 * 1024 * 1024);  // 200 MB
    	
        try (Workbook wb = new XSSFWorkbook(in)) {
            Sheet sheet = (sheetName != null && !sheetName.isBlank())
                    ? wb.getSheet(sheetName)
                    : wb.getSheetAt(0);

            if (sheet == null) {
                throw new IllegalArgumentException("Planilha não encontrada: " + sheetName);
            }

            // Mapear cabeçalho -> índice
            Row headerRow = sheet.getRow(headerRowIndex);
            if (headerRow == null) {
                throw new IllegalArgumentException("Linha de cabeçalho não encontrada no índice " + headerRowIndex);
            }
            Map<String, Integer> headerIndex = readHeaderIndex(headerRow);

            // Preparar metadados dos campos anotados
            List<FieldBinding> bindings = prepareBindings(type, headerIndex);

            FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();
            DataFormatter formatter = new DataFormatter(Locale.getDefault());

            ArrayList<T> result = new ArrayList<>();
            for (int r = headerRowIndex + 1; r <= sheet.getLastRowNum(); r++) {
                Row row = sheet.getRow(r);
                if (row == null) {
                    if (!skipEmptyRows) result.add(type.getDeclaredConstructor().newInstance());
                    continue;
                }

                // Se quiser pular linhas completamente vazias:
                if (skipEmptyRows && isRowCompletelyEmpty(row)) continue;

                T instance = type.getDeclaredConstructor().newInstance();

                for (FieldBinding fb : bindings) {
                    Cell cell = getCell(row, fb.colIndex);
                    Object value = convertCellValue(cell, evaluator, formatter, fb);
                    if (value == null && fb.required) {
                        throw new IllegalStateException(
                                "Campo obrigatório vazio: " + fb.field.getName() +
                                " (linha " + (r + 1) + ", coluna " + (fb.colIndex + 1) + ")"
                        );
                    }
                    if (value != null) {
                        fb.field.setAccessible(true);
                        fb.field.set(instance, value);
                    }
                    else
                    {
                        fb.field.setAccessible(true);
                        fb.field.set(instance, "");
                    }
                }

                result.add(instance);
            }

            return result;
        } catch (RuntimeException re) {
            throw re;
        } catch (Exception e) {
            throw new RuntimeException("Falha ao ler a planilha: " + e.getMessage(), e);
        }
    }

    // --- helpers ---

    private static Map<String, Integer> readHeaderIndex(Row headerRow) {
        Map<String, Integer> map = new HashMap<>();
        DataFormatter df = new DataFormatter(Locale.getDefault());
        for (int c = 0; c < headerRow.getLastCellNum(); c++) {
            Cell cell = headerRow.getCell(c);
            String text = cell == null ? "" : df.formatCellValue(cell).trim();
            if (!text.isEmpty()) {
                map.put(normalize(text), c);
            }
        }
        return map;
    }

    private static String normalize(String s) {
        return s.trim().toLowerCase(Locale.ROOT);
    }

    private static <T> List<FieldBinding> prepareBindings(Class<T> type, Map<String, Integer> headerIndex) {
        List<FieldBinding> bindings = new ArrayList<>();
        for (Field f : type.getDeclaredFields()) {
            ExcelColumn ann = f.getAnnotation(ExcelColumn.class);
            if (ann == null) continue;

            int colIndex = -1;
            if (!ann.header().isBlank()) {
                Integer idx = headerIndex.get(normalize(ann.header()));
                if (idx == null && ann.index() < 0) {
                    throw new IllegalArgumentException(
                            "Cabeçalho não encontrado para campo " + f.getName() +
                            " -> '" + ann.header() + "'"
                    );
                }
                colIndex = (idx != null) ? idx : ann.index();
            } else if (ann.index() >= 0) {
                colIndex = ann.index();
            } else {
                throw new IllegalArgumentException("Defina header ou index para o campo " + f.getName());
            }

            bindings.add(new FieldBinding(f, colIndex, ann.required(), ann.pattern(), ann.digitsOnly()));
        }
        return bindings;
    }

    private static boolean isRowCompletelyEmpty(Row row) {
        for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
            Cell cell = row.getCell(c);
            if (cell != null && cell.getCellType() != CellType.BLANK) {
                if (cell.getCellType() == CellType.STRING && !cell.getStringCellValue().trim().isEmpty())
                    return false;
                if (cell.getCellType() != CellType.STRING)
                    return false;
            }
        }
        return true;
    }

    private static Cell getCell(Row row, int colIndex) {
        return colIndex >= 0 ? row.getCell(colIndex) : null;
    }

    private static Object convertCellValue(
            Cell cell,
            FormulaEvaluator evaluator,
            DataFormatter formatter,
            FieldBinding fb
    ) {
        if (cell == null) return null;

        CellType type = cell.getCellType();
        if (type == CellType.FORMULA) {
            type = evaluator.evaluateFormulaCell(cell);
        }

        Class<?> targetType = fb.field.getType();

        // 1) Datas/horas nativas do Excel (número serial)
        if (type == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
            Date date = cell.getDateCellValue();
            Instant instant = date.toInstant();
            ZoneId zone = ZoneId.systemDefault(); // ajuste se necessário
            if (targetType == LocalDate.class) {
                return instant.atZone(zone).toLocalDate();
            } else if (targetType == LocalTime.class) {
                return instant.atZone(zone).toLocalTime();
            } else if (targetType == LocalDateTime.class) {
                return instant.atZone(zone).toLocalDateTime();
            } else {
                // fallback para texto formatado
            	//System.out.println(cell.getNumericCellValue());

            	//System.out.println(cell.getCellType());
            	//System.out.println(cell.getCellStyle().getDataFormatString());

            	if(fb.pattern.equals("mmm/yyyy"))
            	{

            		// converte para LocalDate
        		    LocalDate ld = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        		    // formata explicitamente em pt-BR
        		    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MMM/yyyy", Locale.of("pt", "BR"));

        		    String txt = ld.format(fmt).toLowerCase(); // jan/2023
        		    return txt;

            	}
            	else if(fb.pattern.equals("dd/MM/yyyy"))
            	{
            		// converte para LocalDate
        		    LocalDate ld = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        		    // formata explicitamente em pt-BR
        		    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        		    String txt = ld.format(fmt).toLowerCase(); // jan/2023
        		    return txt;

            	}
            		
            	else
            	{
                	String txt = formatter.formatCellValue(cell, evaluator);
                    //String txt = formatter.formatCellValue(cell);
                    return coerceFromString(txt, targetType, fb.pattern);	
            	}
            }
        }

        // 2) Numérico
        if (type == CellType.NUMERIC) {
           // double d = cell.getNumericCellValue();
            //return coerceFromNumber(d, targetType);

			CellRead cr = readCellValue(cell, evaluator, formatter);
			Object coerced = coerce(cell, cr, fb);

        }

        // 3) Boolean
        if (type == CellType.BOOLEAN) {
            boolean b = cell.getBooleanCellValue();
            if (targetType == Boolean.class || targetType == boolean.class) return b;
            return String.valueOf(b); // ou null
        }

        // 4) String (inclui células com hora "08:30" como texto)
        String text = formatter.formatCellValue(cell).trim();
        if (text.isEmpty()) return null;

        // Se destino é data/hora, usar padrão se fornecido
        return coerceFromString(text, targetType, fb.pattern);
    }
    

	private static Object coerce(Cell cell, CellRead cr, FieldBinding fb) {
	    if (cr == null || (cr.raw == null && (cr.text == null || cr.text.isBlank()))) return null;
	    Class<?> target = fb.field.getType();
	
	    // ---- String (inclui 'digitsOnly' e campos livres) ----
	    if (target == String.class) {
	        // Se a célula for numérica, pegue texto "plain" para evitar científico
	        String s = PoiNumberUtil.numericCellAsPlainString(cell);
	        if (s == null) {
	            s = (cr.text != null ? cr.text : String.valueOf(cr.raw));
	        }
	        if (s == null) return null;
	        s = s.trim();
	
	        if (fb.digitsOnly) {
	            s = s.replaceAll("\\D+", "");
	            return s.isEmpty() ? null : s;
	        }
	        return s.isEmpty() ? null : s;
	    }
	
	    // ---- Numéricos (int/long/double/...) usando caminho anti-científico ----
	    if (Number.class.isAssignableFrom(target) || target.isPrimitive()) {
	        return coerceNumberFromCell(cell, cr, target);
	    }
	
	    // ---- Boolean ----
	    if (target == Boolean.class || target == boolean.class) {
	        if (cr.raw instanceof Boolean) return cr.raw;
	        String s = (cr.text != null ? cr.text : String.valueOf(cr.raw));
	        if (s == null) return null;
	        s = s.trim().toLowerCase(java.util.Locale.ROOT);
	        if (java.util.Set.of("true","t","1","sim","yes","y","on").contains(s)) return true;
	        if (java.util.Set.of("false","f","0","nao","não","no","n","off").contains(s)) return false;
	        return null;
	    }
	
	    // ---- Enum ----
	    if (Enum.class.isAssignableFrom(target)) {
	        String s = (cr.text != null ? cr.text : String.valueOf(cr.raw));
	        if (s == null) return null;
	        return enumFromString(target, s);
	    }
	
	    // ---- Datas/horas (se você usar) ----
	    // if (target == LocalTime.class) { ... }
	    // if (target == LocalDateTime.class) { ... }
	
	    // fallback
	    return cr.raw != null ? cr.raw : cr.text;
	}


    private static Object coerceFromNumber(Cell cell, CellRead cr, Class<?> target) {

    	// 1) Se for numérica, tente pegar "plain" primeiro:
    	    String plain = PoiNumberUtil.numericCellAsPlainString(cell);
    	    if (plain != null) {
    	        try {
    	            // Remove separadores se houver (geralmente plain já vem só com dígitos e possível ponto decimal)
    	            String norm = plain.replace("_", "").trim();
    	            if (target == Integer.class || target == int.class)   return Integer.parseInt(norm);
    	            if (target == Long.class    || target == long.class)  return Long.parseLong(norm);
    	            if (target == Short.class   || target == short.class) return Short.parseShort(norm);
    	            if (target == Byte.class    || target == byte.class)  return Byte.parseByte(norm);
    	            if (target == Double.class  || target == double.class)return Double.parseDouble(norm);
    	            if (target == Float.class   || target == float.class) return Float.parseFloat(norm);
    	        } catch (NumberFormatException e) {
    	            // fallback para o fluxo existente
    	        }
    	    }

    	    // 2) Caso contrário, use seu fluxo já existente (raw Number, depois texto):
    	    if (cr.raw instanceof Number) {
    	        return coerceNumber((Number) cr.raw, target);
    	    }
    	    if (cr.text != null) {
    	        return coerceNumberFromString(cr.text, target);
    	    }
    	    return null;

    }

    private static Object coerceFromString(String text, Class<?> targetType, String pattern) {
        if (targetType == String.class) return text;

        if (targetType == Integer.class || targetType == int.class)    return parseIntSafe(text);
        if (targetType == Long.class    || targetType == long.class)   return parseLongSafe(text);
        if (targetType == Double.class  || targetType == double.class) return parseDoubleSafe(text);
        if (targetType == Float.class   || targetType == float.class)  return (float) parseDoubleSafe(text);
        if (targetType == BigDecimal.class)                            return parseBigDecimalSafe(text);
        if (targetType == Boolean.class || targetType == boolean.class) return parseBooleanSafe(text);

        if (targetType == LocalDate.class) {
            if (!pattern.isBlank()) {
                return LocalDate.parse(text, DateTimeFormatter.ofPattern(pattern));
            }
            // tentativas comuns
            return tryParseLocalDateCommon(text);
        }
        if (targetType == LocalTime.class) {
            if (!pattern.isBlank()) {
                return LocalTime.parse(text, DateTimeFormatter.ofPattern(pattern));
            }
            // "08:30" etc
            return tryParseLocalTimeCommon(text);
        }
        if (targetType == LocalDateTime.class) {
            if (!pattern.isBlank()) {
                return LocalDateTime.parse(text, DateTimeFormatter.ofPattern(pattern));
            }
            return tryParseLocalDateTimeCommon(text);
        }

        if (Enum.class.isAssignableFrom(targetType)) {
            return enumFromString(targetType, text);
        }

        // fallback
        return text;
    }

    private static Integer parseIntSafe(String s) {
        s = s.replaceAll("\\s", "").replace(".", "").replace(",", ".");
        try { return (int) Double.parseDouble(s); } catch (Exception e) { return null; }
    }
    private static Long parseLongSafe(String s) {
        s = s.replaceAll("\\s", "").replace(".", "").replace(",", ".");
        try { return (long) Double.parseDouble(s); } catch (Exception e) { return null; }
    }
    private static double parseDoubleSafe(String s) {
        s = s.replaceAll("\\s", "").replace(".", "").replace(",", ".");
        return Double.parseDouble(s);
    }
    private static BigDecimal parseBigDecimalSafe(String s) {
        // Se sua cultura usa vírgula, ajuste conforme necessário
        String normalized = s.replace(".", "").replace(',', '.').replaceAll("\\s", "");
        return new BigDecimal(normalized);
    }
    private static Boolean parseBooleanSafe(String s) {
        String n = s.trim().toLowerCase(Locale.ROOT);
        return Set.of("true","t","1","sim","yes","y","ativo","on").contains(n) ? Boolean.TRUE :
               Set.of("false","f","0","nao","não","no","n","inativo","off").contains(n) ? Boolean.FALSE : null;
    }

    private static LocalDate tryParseLocalDateCommon(String s) {
        List<String> patterns = List.of("dd/MM/yyyy", "yyyy-MM-dd", "dd-MM-yyyy");
        for (String p : patterns) {
            try { return LocalDate.parse(s, DateTimeFormatter.ofPattern(p)); } catch (Exception ignored) {}
        }
        return null;
    }

    private static LocalTime tryParseLocalTimeCommon(String s) {
        List<String> patterns = List.of("HH:mm:ss", "HH:mm");
        for (String p : patterns) {
            try { return LocalTime.parse(s, DateTimeFormatter.ofPattern(p)); } catch (Exception ignored) {}
        }
        return null;
    }

    private static LocalDateTime tryParseLocalDateTimeCommon(String s) {
        List<String> patterns = List.of("dd/MM/yyyy HH:mm:ss", "dd/MM/yyyy HH:mm", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd'T'HH:mm:ss");
        for (String p : patterns) {
            try { return LocalDateTime.parse(s, DateTimeFormatter.ofPattern(p)); } catch (Exception ignored) {}
        }
        return null;
    }

    @SuppressWarnings({"rawtypes","unchecked"})
    private static Object enumFromString(Class<?> enumType, String raw) {
        String key = raw.trim().toUpperCase(Locale.ROOT).replace(' ', '_');
        for (Object c : ((Class<? extends Enum>) enumType).getEnumConstants()) {
            if (((Enum) c).name().equalsIgnoreCase(key)) return c;
        }
        // tenta por toString()
        for (Object c : ((Class<? extends Enum>) enumType).getEnumConstants()) {
            if (c.toString().equalsIgnoreCase(raw)) return c;
        }
        return null; // ou lance exceção, se preferir
    }

    private static class FieldBinding {
        final Field field;
        final int colIndex;
        final boolean required;
        final String pattern;
        final boolean digitsOnly;

        FieldBinding(Field field, int colIndex, boolean required, String pattern, boolean digitsOnly) {
            this.field = field;
            this.colIndex = colIndex;
            this.required = required;
            this.pattern = pattern;
            this.digitsOnly = digitsOnly;
        }
    }
    

	public static <T> T ifNull(T value, T defaultValue) {
	    return value == null ? defaultValue : value;
	}
	

	static class CellRead {
	    final Object raw;   // Date, Double, Boolean, String etc.
	    final String text;  // DataFormatter.formatCellValue(cell)
	
	    CellRead(Object raw, String text) {
	        this.raw = raw;
	        this.text = text;
	    }
	}
	

	private static CellRead readCellValue(Cell cell, FormulaEvaluator evaluator, DataFormatter formatter) {

		if (cell == null) return new CellRead(null, null);
		
	    CellType t = cell.getCellType();
	    if (t == CellType.FORMULA) t = evaluator.evaluateFormulaCell(cell);
	
	    String formatted = formatter.formatCellValue(cell).trim();
	
	    switch (t) {
	        case BLANK:
	            return new CellRead(null, formatted.isEmpty() ? null : formatted);
	        case STRING:
	            return new CellRead(formatted, formatted);
	        case BOOLEAN:
	            return new CellRead(cell.getBooleanCellValue(), formatted);
	        case NUMERIC:
	            if (DateUtil.isCellDateFormatted(cell)) {
	                return new CellRead(cell.getDateCellValue(), formatted);
	            } else {
	                return new CellRead(cell.getNumericCellValue(), formatted);
	            }
	        default:
	            return new CellRead(formatted, formatted);
	    }
	}
	

	private static Object coerceNumber(Number n, Class<?> target) {
	    if (target == Integer.class || target == int.class)    return n.intValue();
	    if (target == Long.class    || target == long.class)   return n.longValue();
	    if (target == Double.class  || target == double.class) return n.doubleValue();
	    if (target == Float.class   || target == float.class)  return n.floatValue();
	    if (target == Short.class   || target == short.class)  return n.shortValue();
	    if (target == Byte.class    || target == byte.class)   return n.byteValue();
	    if (target.getName().equals("java.math.BigDecimal"))   return new java.math.BigDecimal(n.toString());
	    return n;
	}


	private static Object coerceNumberFromString(String s, Class<?> target) {
	    if (s == null) return null;
	    s = s.trim();
	    if (s.isEmpty()) return null;
	
	    // Normalização simples: remove milhar e usa ponto como decimal
	    String norm = s.replace(".", "").replace(",", ".").replaceAll("\\s", "");
	
	    try {
	        if (target == Integer.class || target == int.class)    return Integer.parseInt(norm);
	        if (target == Long.class    || target == long.class)   return Long.parseLong(norm);
	        if (target == Short.class   || target == short.class)  return Short.parseShort(norm);
	        if (target == Byte.class    || target == byte.class)   return Byte.parseByte(norm);
	        if (target == Double.class  || target == double.class) return Double.parseDouble(norm);
	        if (target == Float.class   || target == float.class)  return Float.parseFloat(norm);
	        if (target.getName().equals("java.math.BigDecimal"))   return new java.math.BigDecimal(norm);
	    } catch (NumberFormatException e) {
	        return null;
	    }
	    return null;
	}


	private static Object coerceNumberFromCell(Cell cell, CellRead cr, Class<?> target) {
	    // 1) Tenta texto "plain" da célula numérica (sem notação científica)
	    String plain = PoiNumberUtil.numericCellAsPlainString(cell);
	    if (plain != null) {
	        Object parsed = coerceNumberFromString(plain, target);
	        if (parsed != null) return parsed;
	        // se falhar, continua para tentativas seguintes
	    }
	
	    // 2) Se o raw já é Number, usa coercão direta
	    if (cr != null && cr.raw instanceof Number) {
	        return coerceNumber((Number) cr.raw, target);
	    }
	
	    // 3) Se temos texto (formatado), tenta parsear
	    if (cr != null && cr.text != null) {
	        return coerceNumberFromString(cr.text, target);
	    }
	
	    return null;
	}



}
