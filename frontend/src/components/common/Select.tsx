import React from "react";

interface SelectOption {
    value: string;
    label: string;
}

interface SelectProps extends Omit<React.SelectHTMLAttributes<HTMLSelectElement>, 'onChange'> {
    label?: string;
    options: SelectOption[];
    value: string;
    onChange: (value: string) => void;
    placeholder?: string;
}

export default function Select({
    label,
    options,
    value,
    onChange,
    placeholder = "선택해주세요",
    className = "",
    disabled = false,
    ...props
}: SelectProps) {
    return (
        <div className={`flex flex-col gap-1.5 ${className}`}>
            {label && (
                <label className="text-sm font-semibold text-slate-700 dark:text-slate-300">
                    {label}
                </label>
            )}
            <div className="relative">
                <select
                    value={value}
                    onChange={(e) => onChange(e.target.value)}
                    disabled={disabled}
                    className={`
            w-full appearance-none rounded-xl border border-slate-200 bg-white px-4 py-3
            text-slate-900 transition-colors focus:border-primary focus:outline-none focus:ring-1 focus:ring-primary
            dark:border-slate-700 dark:bg-slate-800 dark:text-slate-100 dark:focus:border-primary
            ${disabled ? "cursor-not-allowed opacity-60" : "cursor-pointer hover:border-slate-300 dark:hover:border-slate-600"}
          `}
                    {...props}
                >
                    <option value="" disabled className="text-slate-400">
                        {placeholder}
                    </option>
                    {options.map((option) => (
                        <option key={option.value} value={option.value}>
                            {option.label}
                        </option>
                    ))}
                </select>
                <div className="pointer-events-none absolute inset-y-0 right-4 flex items-center justify-center text-slate-400">
                    <span className="material-symbols-outlined text-[20px]">
                        expand_more
                    </span>
                </div>
            </div>
        </div>
    );
}
