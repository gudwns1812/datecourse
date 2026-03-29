"use client";

import Link from "next/link";
import { useAuthStore } from "@/store/useAuthStore";
import Button from "@/components/common/Button";

export default function Header() {
  const { isLoggedIn, authChecked, username, logout } = useAuthStore();

  return (
    <header className="sticky top-0 z-50 w-full border-b border-primary/10 bg-background-light/80 dark:bg-background-dark/80 backdrop-blur-md px-6 md:px-10 py-4">
      <div className="max-w-7xl mx-auto flex items-center justify-between">
        <Link href="/" className="flex items-center gap-3">
          <div className="p-2 bg-primary rounded-lg text-white">
            <span className="material-symbols-outlined text-2xl">subway</span>
          </div>
          <h1 className="text-2xl font-extrabold tracking-tight text-primary">
            DateCourse
          </h1>
        </Link>
        
        <nav className="hidden md:flex items-center gap-8">
          <Link
            href="/stations/random"
            className="text-sm font-semibold hover:text-primary transition-colors"
          >
            랜덤역 뽑기
          </Link>
          <Link
            href="/stations/map"
            className="text-sm font-semibold hover:text-primary transition-colors"
          >
            지도 보기
          </Link>
          <Link
            href="#"
            className="text-sm font-semibold hover:text-primary transition-colors"
          >
            커뮤니티
          </Link>
        </nav>

        <div className="flex items-center gap-3">
          {!authChecked ? (
            <div className="h-10 w-24 rounded-xl bg-primary/10 animate-pulse" />
          ) : isLoggedIn ? (
            <div className="flex items-center gap-4">
              <div className="hidden md:flex items-center gap-2 px-4 py-2 bg-primary/5 rounded-xl border border-primary/10">
                <span className="material-symbols-outlined text-primary text-xl">account_circle</span>
                <span className="text-sm font-bold text-slate-700 dark:text-slate-300">
                  {username}님
                </span>
              </div>
              <Button variant="outline" size="sm" onClick={logout}>
                로그아웃
              </Button>
            </div>
          ) : (
            <div className="flex items-center gap-2">
              <Link href="/login">
                <Button variant="ghost" size="sm">로그인</Button>
              </Link>
              <Link href="/signup">
                <Button size="sm">회원가입</Button>
              </Link>
            </div>
          )}
          
          <Link
            href="/stations/map"
            className="p-2.5 rounded-xl bg-primary/10 text-primary hover:bg-primary/20 transition-all ml-2"
            aria-label="지도 보기"
          >
            <span className="material-symbols-outlined text-[24px]">
              map
            </span>
          </Link>
        </div>
      </div>
    </header>
  );
}
