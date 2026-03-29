import Link from "next/link";

export default function Footer() {
    return (
        <footer className="mt-auto border-t border-slate-200 dark:border-slate-800 py-10 px-6 md:px-10">
            <div className="max-w-7xl mx-auto flex flex-col md:flex-row justify-between items-center gap-6">
                <div className="flex items-center gap-2">
                    <span className="material-symbols-outlined text-primary">subway</span>
                    <span className="font-bold text-slate-700 dark:text-slate-300">
            DateCourse
          </span>
                </div>
                <p className="text-sm text-slate-500">
                    © 2026 DateCourse. Seoul Metro Random Dating Spot Service.
                </p>
                <div className="flex gap-6">
                    <Link
                        href="#"
                        className="text-sm text-slate-500 hover:text-primary transition-colors"
                    >
                        이용약관
                    </Link>
                    <Link
                        href="#"
                        className="text-sm text-slate-500 hover:text-primary transition-colors"
                    >
                        개인정보처리방침
                    </Link>
                </div>
            </div>
        </footer>
    );
}
