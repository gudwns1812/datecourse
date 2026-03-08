"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";
import Link from "next/link";
import Button from "@/components/common/Button";
import Input from "@/components/common/Input";
import { authService } from "@/services/auth";

export default function SignupPage() {
  const [formData, setFormData] = useState({
    username: "",
    loginId: "",
    password: "",
    email: "",
    gender: "M",
    phoneNumber: "",
  });
  
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  
  const router = useRouter();

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(null);
    setIsLoading(true);

    try {
      const response = await authService.signup(formData);
      if (response.result === "SUCCESS") {
        alert("회원가입이 완료되었습니다. 로그인해주세요!");
        router.push("/login");
      } else {
        setError("이미 사용 중인 아이디이거나 입력 정보가 올바르지 않습니다.");
      }
    } catch (err) {
      setError("회원가입 중 오류가 발생했습니다. 다시 시도해주세요.");
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="flex-1 flex items-center justify-center px-6 py-12">
      <div className="w-full max-w-lg bg-white dark:bg-slate-900 rounded-3xl p-8 md:p-10 shadow-2xl border border-primary/5">
        <div className="text-center mb-10">
          <div className="inline-flex items-center justify-center p-3 bg-primary/10 rounded-2xl text-primary mb-4">
            <span className="material-symbols-outlined text-3xl">person_add</span>
          </div>
          <h1 className="text-3xl font-extrabold text-slate-900 dark:text-slate-100 mb-2">회원가입</h1>
          <p className="text-slate-500 dark:text-slate-400">어디역?의 회원이 되어 서울 곳곳을 탐험하세요.</p>
        </div>

        <form onSubmit={handleSubmit} className="space-y-5">
          <div className="grid grid-cols-1 md:grid-cols-2 gap-5">
            <div className="space-y-2">
              <label className="text-sm font-bold text-slate-700 dark:text-slate-300 ml-1">이름</label>
              <Input
                name="username"
                placeholder="홍길동"
                value={formData.username}
                onChange={handleChange}
                required
              />
            </div>
            <div className="space-y-2">
              <label className="text-sm font-bold text-slate-700 dark:text-slate-300 ml-1">아이디</label>
              <Input
                name="loginId"
                placeholder="사용할 아이디"
                value={formData.loginId}
                onChange={handleChange}
                required
              />
            </div>
          </div>

          <div className="space-y-2">
            <label className="text-sm font-bold text-slate-700 dark:text-slate-300 ml-1">비밀번호</label>
            <Input
              name="password"
              type="password"
              placeholder="비밀번호를 입력하세요"
              value={formData.password}
              onChange={handleChange}
              required
              fullWidth
            />
          </div>

          <div className="space-y-2">
            <label className="text-sm font-bold text-slate-700 dark:text-slate-300 ml-1">이메일</label>
            <Input
              name="email"
              type="email"
              placeholder="test@example.com"
              value={formData.email}
              onChange={handleChange}
              required
              fullWidth
            />
          </div>

          <div className="grid grid-cols-1 md:grid-cols-2 gap-5">
            <div className="space-y-2">
              <label className="text-sm font-bold text-slate-700 dark:text-slate-300 ml-1">성별</label>
              <div className="flex gap-4">
                <label className="flex-1 flex items-center justify-center gap-2 p-3 bg-primary/5 rounded-xl border-2 border-transparent has-[:checked]:border-primary has-[:checked]:bg-primary/10 cursor-pointer transition-all">
                  <input
                    type="radio"
                    name="gender"
                    value="M"
                    checked={formData.gender === "M"}
                    onChange={handleChange}
                    className="hidden"
                  />
                  <span className="font-bold text-slate-700 dark:text-slate-300">남성</span>
                </label>
                <label className="flex-1 flex items-center justify-center gap-2 p-3 bg-primary/5 rounded-xl border-2 border-transparent has-[:checked]:border-primary has-[:checked]:bg-primary/10 cursor-pointer transition-all">
                  <input
                    type="radio"
                    name="gender"
                    value="F"
                    checked={formData.gender === "F"}
                    onChange={handleChange}
                    className="hidden"
                  />
                  <span className="font-bold text-slate-700 dark:text-slate-300">여성</span>
                </label>
              </div>
            </div>
            <div className="space-y-2">
              <label className="text-sm font-bold text-slate-700 dark:text-slate-300 ml-1">전화번호</label>
              <Input
                name="phoneNumber"
                placeholder="010-1234-5678"
                value={formData.phoneNumber}
                onChange={handleChange}
                required
              />
            </div>
          </div>

          {error && (
            <p className="text-sm font-bold text-primary text-center bg-primary/5 py-3 rounded-xl border border-primary/10 mt-4">
              {error}
            </p>
          )}

          <Button
            type="submit"
            size="lg"
            className="w-full mt-6"
            disabled={isLoading}
          >
            {isLoading ? "처리 중..." : "가입하기"}
          </Button>
        </form>

        <div className="mt-8 text-center text-sm border-t border-slate-100 dark:border-slate-800 pt-8">
          <p className="text-slate-500 dark:text-slate-400">
            이미 계정이 있으신가요?{" "}
            <Link href="/login" className="text-primary font-bold hover:underline">
              로그인하기
            </Link>
          </p>
        </div>
      </div>
    </div>
  );
}
