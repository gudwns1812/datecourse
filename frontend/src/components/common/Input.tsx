import { InputHTMLAttributes, ReactNode } from "react";

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
    <div className={`relative flex items-stretch rounded-xl bg-primary/5 dark:bg-primary/10 overflow-hidden ${fullWidth ? 'w-full' : ''} ${className}`}>
      {icon && (
        <div className="text-primary flex items-center justify-center pl-4">
          <span className="material-symbols-outlined text-[20px]">{icon}</span>
        </div>
      )}
      <input
        className="w-full border-none bg-transparent focus:ring-0 px-4 py-2.5 text-sm font-normal placeholder:text-primary/50 text-slate-900 dark:text-slate-100"
        {...props}
      />
    </div>
  );
}
