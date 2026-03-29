import { InputHTMLAttributes } from "react";

interface InputProps extends InputHTMLAttributes<HTMLInputElement> {
  icon?: string;
  className?: string;
  fullWidth?: boolean;
}

export default function Input({
  icon,
  className = "",
  fullWidth = false,
  ...props
}: InputProps) {
  return (
    <div className={`
      relative flex items-stretch rounded-xl bg-primary/5 dark:bg-primary/10 
      border-2 border-transparent transition-all duration-200
      focus-within:border-primary/30 focus-within:bg-white dark:focus-within:bg-slate-800
      focus-within:shadow-[0_0_0_4px_rgba(var(--primary-rgb),0.1)]
      ${fullWidth ? 'w-full' : ''} ${className}
    `}>
      {icon && (
        <div className="text-primary/60 flex items-center justify-center pl-4 transition-colors group-focus-within:text-primary">
          <span className="material-symbols-outlined text-[20px]">{icon}</span>
        </div>
      )}
      <input
        className="w-full border-none bg-transparent outline-none focus:ring-0 px-4 py-2.5 text-sm font-medium placeholder:text-primary/30 text-slate-900 dark:text-slate-100"
        {...props}
      />
    </div>
  );
}
