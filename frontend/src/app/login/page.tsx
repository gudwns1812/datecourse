"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";
import Link from "next/link";
import Button from "@/components/common/Button";
import Input from "@/components/common/Input";
import { authService } from "@/services/auth";
import { useAuthStore } from "@/store/useAuthStore";

export default function LoginPage() {
  const [loginId, setLoginId] = useState("");
  const [password, setPassword] = useState("");
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  
  const router = useRouter();
  const login = useAuthStore((state) => state.login);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(null);
    setIsLoading(true);

    try {
      const response = await authService.login({ loginId, password });
      if (response.result === "SUCCESS") {
        login(response.data); // data is username
        router.push("/");
      } else {
        setError("아이디 또는 비밀번호를 확인해주세요.");
      }
    } catch (err) {
      setError("로그인 중 오류가 발생했습니다. 다시 시도해주세요.");
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="flex-1 flex items-center justify-center px-6 py-12">
      <div className="w-full max-w-md bg-white dark:bg-slate-900 rounded-3xl p-8 md:p-10 shadow-2xl border border-primary/5">
        <div className="text-center mb-10">
          <div className="inline-flex items-center justify-center p-3 bg-primary/10 rounded-2xl text-primary mb-4">
            <span className="material-symbols-outlined text-3xl">login</span>
          </div>
          <h1 className="text-3xl font-extrabold text-slate-900 dark:text-slate-100 mb-2">반가워요!</h1>
          <p className="text-slate-500 dark:text-slate-400">어디역?에 로그인하고 데이트 장소를 추천받으세요.</p>
        </div>

        <form onSubmit={handleSubmit} className="space-y-6">
          <div className="space-y-2">
            <label className="text-sm font-bold text-slate-700 dark:text-slate-300 ml-1">아이디</label>
            <Input
              icon="person"
              placeholder="아이디를 입력하세요"
              value={loginId}
              onChange={(e) => setLoginId(e.target.value)}
              required
              fullWidth
            />
          </div>

          <div className="space-y-2">
            <label className="text-sm font-bold text-slate-700 dark:text-slate-300 ml-1">비밀번호</label>
            <Input
              type="password"
              icon="lock"
              placeholder="비밀번호를 입력하세요"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
              fullWidth
            />
          </div>

          {error && (
            <p className="text-sm font-bold text-primary text-center bg-primary/5 py-3 rounded-xl border border-primary/10">
              {error}
            </p>
          )}

          <Button
            type="submit"
            size="lg"
            className="w-full"
            disabled={isLoading}
          >
            {isLoading ? "로그인 중..." : "로그인"}
          </Button>
        </form>

        <div className="mt-10 text-center text-sm">
          <p className="text-slate-500 dark:text-slate-400">
            아직 회원이 아니신가요?{" "}
            <Link href="/signup" className="text-primary font-bold hover:underline">
              가입하기
            </Link>
          </p>
        </div>
      </div>
    </div>
  );
}
