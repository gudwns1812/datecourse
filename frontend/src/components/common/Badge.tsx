interface BadgeProps {
  children: React.ReactNode;
  variant?: "primary" | "secondary" | "subway";
  lineColor?: string; // For subway line (e.g., #9966cc)
  className?: string;
}

export default function Badge({
  children,
  variant = "primary",
  lineColor,
  className = "",
}: BadgeProps) {
  const baseStyles = "inline-flex items-center gap-1.5 px-3 py-1 rounded-full text-xs font-bold transition-colors";
  
  const variants = {
    primary: "bg-primary/10 text-primary border border-primary/20",
    secondary: "bg-slate-100 dark:bg-slate-800 text-slate-600 dark:text-slate-400 border border-slate-200 dark:border-slate-700",
    subway: "", // Dynamic styles below
  };

  const dynamicStyle = variant === "subway" && lineColor 
    ? { backgroundColor: `${lineColor}1A`, color: lineColor, borderColor: `${lineColor}33` } 
    : {};

  return (
    <span
      className={`${baseStyles} ${variants[variant]} ${variant === "subway" ? "border" : ""} ${className}`}
      style={dynamicStyle}
    >
      {variant === "subway" && lineColor && (
        <span 
          className="w-2 h-2 rounded-full" 
          style={{ backgroundColor: lineColor }}
        />
      )}
      {children}
    </span>
  );
}
