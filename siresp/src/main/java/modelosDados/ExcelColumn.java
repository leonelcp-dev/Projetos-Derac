package modelosDados;
import java.lang.annotation.*;



@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ExcelColumn {
    String header() default "";  // Nome do cabeçalho
    int index() default -1;      // Alternativa: por índice
    boolean required() default false;
    String pattern() default ""; // p.ex. "HH:mm", "dd/MM/yyyy"
    boolean digitsOnly() default false; // remove tudo que não for [0-9]
}
